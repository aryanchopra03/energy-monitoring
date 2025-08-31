package com.mycompany.gateway.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.cors.reactive.CorsWebFilter;

import java.util.Arrays;

@Configuration
public class CorsConfig {

    @Bean
    public CorsWebFilter corsWebFilter() {
        CorsConfiguration config = new CorsConfiguration();

        // allows your frontend origin(s)
        config.setAllowedOrigins(Arrays.asList(
                "http://localhost:3000", // react local dev
                "http://localhost:5173" // vite local dev
        ));

        // allows credentials (Jwt cookies if needed)
        config.setAllowCredentials(true);

        // allows headers
        config.setAllowedHeaders(Arrays.asList("*"));

        // allows methods
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return new CorsWebFilter(source);
    }
}
