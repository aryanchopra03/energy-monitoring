package com.mycompany.gateway.configuration;

import com.mycompany.gateway.filtter.AuthenticationFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;

public class GatewayConfig {

    public RouteLocator routes (RouteLocatorBuilder builder, AuthenticationFilter authFilter ) {
        return builder.routes()
                .route("auth-service", r -> r.path("/auth/**")
                        .uri("http://localhost:8081"))
                .route("sensor-service", r -> r.path("/sensors/**")
                        .filters(f -> f.filter(authFilter.apply(new AuthenticationFilter.Config())))
                        .uri("http://localhost:8082"))
                .route("report-service", r -> r.path("/reports/**")
                        .filters(f -> f.filter(authFilter.apply(new AuthenticationFilter.Config())))
                        .uri("http:/localhost:8083"))
                .build();
    }
}
