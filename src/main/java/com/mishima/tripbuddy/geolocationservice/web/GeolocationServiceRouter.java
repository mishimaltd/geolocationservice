package com.mishima.tripbuddy.geolocationservice.web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;


@Configuration
public class GeolocationServiceRouter {

    @Bean
    public RouterFunction<ServerResponse> route(GeolocationHandler handler) {
        return RouterFunctions.route(GET("/geolocations/{username}/latest").and(accept(APPLICATION_JSON)), handler::getLatestByUserName)
                .and(RouterFunctions.route(GET("/geolocations/{username}").and(accept(APPLICATION_JSON)), handler::getByUsername))
                .and(RouterFunctions.route(PUT("/geolocations").and(accept(APPLICATION_JSON)).and(contentType(APPLICATION_JSON)), handler::save))
                .and(RouterFunctions.route(DELETE("/geolocations/{id}").and(accept(APPLICATION_JSON)), handler::delete));
    }
}
