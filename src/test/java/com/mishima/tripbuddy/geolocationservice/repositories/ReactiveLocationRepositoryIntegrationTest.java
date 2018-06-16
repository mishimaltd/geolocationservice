package com.mishima.tripbuddy.geolocationservice.repositories;

import com.mishima.tripbuddy.geolocationservice.entity.Geolocation;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class ReactiveLocationRepositoryIntegrationTest {

    @Autowired
    private ReactiveGeolocationRepository repository;

    @Autowired
    private ReactiveMongoOperations operations;

    @Before
    public void setUp() {
        operations.collectionExists(Geolocation.class)
                .flatMap(exists -> exists ? operations.dropCollection(Geolocation.class) : Mono.just(exists))
                .flatMap(o -> operations.createCollection(Geolocation.class)
                        .then(operations.indexOps(Geolocation.class).ensureIndex(new Index().on("username", Sort.Direction.ASC).on("timestamp", Sort.Direction.DESC))))
                .then().block();

        repository.saveAll(Flux.just(
                Geolocation.builder().username("test").latitude(38d).longitude(-79d).accuracy(0d).altitude(10d).altitudeAccuracy(0d).heading(180d).speed(3d).timestamp(1).build(),
                Geolocation.builder().username("test").latitude(39d).longitude(-79d).accuracy(0d).altitude(10d).altitudeAccuracy(0d).heading(180d).speed(3d).timestamp(2).build(),
                Geolocation.builder().username("test").latitude(40d).longitude(-79d).accuracy(0d).altitude(10d).altitudeAccuracy(0d).heading(180d).speed(3d).timestamp(3).build(),
                Geolocation.builder().build())).then().block();
    }

    @After
    public void tearDown() {
        operations.dropCollection(Geolocation.class);
    }

    @Test
    public void findLatestByUsername() {
        Geolocation location = repository.findFirstByUsernameOrderByTimestampDesc("test").block();
        assertNotNull(location);
        assertEquals("test", location.getUsername());
        assertEquals(40d, location.getLatitude(), 0d);
    }

    @Test
    public void findLatestByInvalidUsername() {
        assertNull(repository.findFirstByUsernameOrderByTimestampDesc("invalid").switchIfEmpty(Mono.empty()).block());
    }



}
