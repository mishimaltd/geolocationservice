package com.mishima.tripbuddy.geolocationservice.repositories;

import com.mishima.tripbuddy.geolocationservice.entity.Geolocation;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface ReactiveGeolocationRepository extends ReactiveCrudRepository<Geolocation,String> {

    Mono<Geolocation> findFirstByUsernameOrderByTimestampDesc(String username);

    Flux<Geolocation> findByUsernameOrderByTimestampDesc(String username);

}
