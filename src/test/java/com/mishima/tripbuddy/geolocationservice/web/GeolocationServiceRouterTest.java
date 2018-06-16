package com.mishima.tripbuddy.geolocationservice.web;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mishima.tripbuddy.geolocationservice.entity.Geolocation;
import com.mishima.tripbuddy.geolocationservice.repositories.ReactiveGeolocationRepository;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
public class GeolocationServiceRouterTest {

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

    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void testGetLatestByUsername() throws Exception {
        String result = webTestClient.get()
                .uri("/geolocation/{username}/latest", "test")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.OK)
                .expectBody(String.class).returnResult().getResponseBody();
        Map<String,Object> map = new ObjectMapper().readValue(result, new TypeReference<Map<String,Object>>(){});
        assertEquals("test", map.get("username"));
        assertEquals(40d, map.get("latitude"));
    }

    @Test
    public void testGetLatestByInvalidUsername() throws Exception {
        webTestClient.get()
                .uri("/geolocation/{username}/latest", "invalid")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.NOT_FOUND);
    }
}
