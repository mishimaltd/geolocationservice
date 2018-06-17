package com.mishima.tripbuddy.geolocationservice.entity;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document
@ToString
@EqualsAndHashCode
@CompoundIndex(def = "{'username':1, 'timestamp':-1}", name = "username_timestamp")
public class Geolocation {

    @Id
    private String _id;
    private String username;
    private double latitude;
    private double longitude;
    private double accuracy;
    private double altitude;
    private double altitudeAccuracy;
    private double heading;
    private double speed;
    private long timestamp;

    private Geolocation() {
    }

    public Geolocation(String username, double latitude, double longitude, double accuracy, double altitude, double altitudeAccuracy, double heading, double speed, long timestamp) {
        this.username = username;
        this.latitude = latitude;
        this.longitude = longitude;
        this.accuracy = accuracy;
        this.altitude = altitude;
        this.altitudeAccuracy = altitudeAccuracy;
        this.heading = heading;
        this.speed = speed;
        this.timestamp = timestamp;
    }

    public static Builder builder() {
        return new Builder();
    }


    public static final class Builder {
        private String username;
        private double latitude;
        private double longitude;
        private double accuracy;
        private double altitude;
        private double altitudeAccuracy;
        private double heading;
        private double speed;
        private long timestamp;

        private Builder() {
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder latitude(double latitude) {
            this.latitude = latitude;
            return this;
        }

        public Builder longitude(double longitude) {
            this.longitude = longitude;
            return this;
        }

        public Builder accuracy(double accuracy) {
            this.accuracy = accuracy;
            return this;
        }

        public Builder altitude(double altitude) {
            this.altitude = altitude;
            return this;
        }

        public Builder altitudeAccuracy(double altitudeAccuracy) {
            this.altitudeAccuracy = altitudeAccuracy;
            return this;
        }

        public Builder heading(double heading) {
            this.heading = heading;
            return this;
        }

        public Builder speed(double speed) {
            this.speed = speed;
            return this;
        }

        public Builder timestamp(long timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public Geolocation build() {
            return new Geolocation(username, latitude, longitude, accuracy, altitude, altitudeAccuracy, heading, speed, timestamp);
        }
    }

}
