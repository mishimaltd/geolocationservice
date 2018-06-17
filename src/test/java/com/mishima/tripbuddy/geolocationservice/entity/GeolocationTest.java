package com.mishima.tripbuddy.geolocationservice.entity;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class GeolocationTest {

    @Test
    public void testSerialization() throws Exception {
        ObjectMapper om = new ObjectMapper();
        Geolocation geolocation = Geolocation.builder().username("saved").latitude(38d).longitude(-79d).accuracy(0d).altitude(10d).altitudeAccuracy(0d).heading(180d).speed(3d).timestamp(1).build();
        String json = om.writeValueAsString(geolocation);
        Geolocation deserialized = om.readValue(json, Geolocation.class);
        assertEquals(geolocation, deserialized);
    }

}
