package com.mycompany.gateway.handler;

import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import org.springframework.core.io.buffer.DataBuffer;
import java.nio.charset.StandardCharsets;
import java.time.Instant;

@Component
@Order(-2) // Ensures it runs before default handlers
public class GatewayExceptionHandler implements ErrorWebExceptionHandler {

    @Override
    public Mono<Void> handle (ServerWebExchange exchange, Throwable ex) {

        String errorMessage = (ex.getMessage() != null) ? ex.getMessage() : "Unexpected error occurred";
        
        // if response is already committed, we can't modify it
        if (exchange.getResponse().isCommitted()) {
            return Mono.error(ex);
        }

        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        // Example: map some exception to specific status codes
        if (ex instanceof SecurityException) {
            status = HttpStatus.UNAUTHORIZED;
        }
        else if (ex instanceof  IllegalArgumentException) {
            status = HttpStatus.BAD_REQUEST;
        }
        else if (ex instanceof ResponseStatusException rse) {
            status = (HttpStatus) rse.getStatusCode();
        }
        else if (ex instanceof org.springframework.web.server.ServerWebInputException) {
            status = HttpStatus.BAD_REQUEST;
        }


        exchange.getResponse().setStatusCode(status);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

        String path = exchange.getRequest().getPath().value();

        String errorJson = String.format(
                "{ \"timestamp\": \"%s\", \"status\": %d, \"error\": \"%s\", \"path\": \"%s\" }",
                Instant.now(), status.value(), ex.getMessage(), path
        );

        DataBuffer buffer = exchange.getResponse()
                .bufferFactory()
                .wrap(errorJson.getBytes(StandardCharsets.UTF_8));

        return exchange.getResponse().writeWith(Mono.just(buffer));
    }
}
