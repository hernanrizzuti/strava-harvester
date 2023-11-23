package com.rizzutih.stravaharvester.web.strava.restclient.builders;

import com.rizzutih.stravaharvester.config.ApplicationConfigProperties;
import com.rizzutih.stravaharvester.config.Endpoints;

public class TestApplicationConfigProperties {

    private String uri;

    private Endpoints endpoints;

    private TestApplicationConfigProperties() {
    }

    public static TestApplicationConfigProperties testApplicationConfigProperties() {
        return new TestApplicationConfigProperties();
    }

    public ApplicationConfigProperties setProperties() {
        ApplicationConfigProperties configProperties = new ApplicationConfigProperties();
        Endpoints endpoints = new Endpoints();
        endpoints.setActivities("athlete/activities");
        configProperties.setEndpoints(endpoints);
        configProperties.setUri("https://www.strava.com/api/v3/");
        return configProperties;
    }

}
