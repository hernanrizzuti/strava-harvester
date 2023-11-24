package com.rizzutih.stravaharvester.web.strava.restclient.builders;

import com.rizzutih.stravaharvester.config.ApplicationConfigProperties;
import com.rizzutih.stravaharvester.config.Endpoints;

public class TestApplicationConfigPropertiesBuilder {

    private String uri;

    private Endpoints endpoints;

    private TestApplicationConfigPropertiesBuilder() {
    }

    public static TestApplicationConfigPropertiesBuilder testApplicationConfigProperties() {
        return new TestApplicationConfigPropertiesBuilder();
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
