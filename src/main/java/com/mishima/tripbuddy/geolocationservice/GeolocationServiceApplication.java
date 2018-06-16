package com.mishima.tripbuddy.geolocationservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@EnableMongoAuditing
@EnableReactiveMongoRepositories
@SpringBootApplication
public class GeolocationServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(GeolocationServiceApplication.class, args);
    }
}
