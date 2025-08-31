package com.mycompany.gateway.filtter;

import io.jsonwebtoken.Claims;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.security.Key;


@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {
    @Value("${jwt.secret}")
    private String secretKey;
    public AuthenticationFilter() {
        super(Config.class);
    }

    public static class Config {
        // No special config for now
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                return this.onError(exchange, "Missing Authorization Header", HttpStatus.UNAUTHORIZED);
            }

            String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
            if (!authHeader.startsWith("Bearer ")) {
                return this.onError(exchange, "Invalid Authorization Header ", HttpStatus.UNAUTHORIZED);
            }

            String token = authHeader.substring(7);

            try {
                Key key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
                Claims claims = Jwts.parserBuilder()
                        .setSigningKey(key)
                        .build()
                        .parseClaimsJws(token)
                        .getBody();

                // Add claims into request headers

                return chain.filter(
                        exchange.mutate()
                                .request(exchange.getRequest().mutate()
                                        .header("X-User-Id", claims.getSubject())
                                        .build())
                                .build()
                );

            }
            catch (ExpiredJwtException e) {
                return this.onError(exchange, "JWT Token Expired", HttpStatus.UNAUTHORIZED);
            }
            catch (Exception e) {
                return this.onError(exchange, "Invalid JWT Token ", HttpStatus.UNAUTHORIZED);
            }
        };
    }

    private Mono<Void> onError(ServerWebExchange exchange, String str, HttpStatus httpStatus) {
        exchange.getResponse().setStatusCode(httpStatus);
        exchange.getResponse().getHeaders().add(HttpHeaders.CONTENT_TYPE, "application/json");

        String errorJson = String.format("{\"error\":\"%s\",\"status\":%d}", str,httpStatus.value());
        byte[] bytes = errorJson.getBytes(StandardCharsets.UTF_8);

        return exchange.getResponse().writeWith(Mono.just(
                exchange.getResponse().bufferFactory().wrap(bytes)
        ));
    }
}
