package com.mishima.tripbuddy.geolocationservice.web;

import com.mishima.tripbuddy.geolocationservice.repositories.ReactiveGeolocationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import javax.annotation.Nonnull;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.BodyInserters.fromObject;


@Component
@Slf4j
public class GeolocationHandler {

    @Autowired
    private ReactiveGeolocationRepository repository;

    @Nonnull
    public Mono<ServerResponse> getLatestByUserName(ServerRequest request) {
        return repository.findFirstByUsernameOrderByTimestampDesc(request.pathVariable("username")).flatMap(geolocation ->
                ServerResponse.ok().contentType(APPLICATION_JSON).body(fromObject(geolocation)))
                    .switchIfEmpty(ServerResponse.notFound().build());
    }
}
