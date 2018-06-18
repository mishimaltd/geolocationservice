package com.mishima.tripbuddy.geolocationservice.web;

import com.mishima.tripbuddy.geolocationservice.entity.Geolocation;
import com.mishima.tripbuddy.geolocationservice.repositories.ReactiveGeolocationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.Nonnull;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.BodyInserters.fromObject;
import static org.springframework.web.reactive.function.BodyInserters.fromPublisher;

@Component
@Slf4j
public class GeolocationServiceHandler {

    @Autowired
    private ReactiveGeolocationRepository repository;

    @Nonnull
    public Mono<ServerResponse> save(ServerRequest request) {
        return request.bodyToMono(Geolocation.class).flatMap(geolocation ->
                ServerResponse.ok().contentType(APPLICATION_JSON).body(fromPublisher(repository.save(geolocation), Geolocation.class)));
    }

    @Nonnull
    public Mono<ServerResponse> getLatestByUserName(ServerRequest request) {
        return repository.findFirstByUsernameOrderByTimestampDesc(request.pathVariable("username")).flatMap(geolocation ->
                ServerResponse.ok().contentType(APPLICATION_JSON).body(fromObject(geolocation)))
                    .switchIfEmpty(ServerResponse.notFound().build());
    }

    @Nonnull
    public Mono<ServerResponse> getByUsername(ServerRequest request) {
        Flux<Geolocation> geolocations = repository.findByUsernameOrderByTimestampDesc(request.pathVariable("username"));
        return ServerResponse.ok().contentType(APPLICATION_JSON).body(geolocations, Geolocation.class);
    }

    @Nonnull
    public Mono<ServerResponse> delete(ServerRequest request) {
        return repository.deleteById(request.pathVariable("id")).then(ServerResponse.ok().build());
    }

}
