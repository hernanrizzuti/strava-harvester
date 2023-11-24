package com.rizzutih.stravaharvester.service;

import com.rizzutih.stravaharvester.client.StravaRestClient;
import com.rizzutih.stravaharvester.config.ApplicationConfigProperties;
import com.rizzutih.stravaharvester.exception.StravaActivitiesResponseException;
import com.rizzutih.stravaharvester.factory.ActivityFactory;
import com.rizzutih.stravaharvester.writer.CustomParquetWriter;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

import static com.rizzutih.stravaharvester.web.strava.restclient.builders.TestApplicationConfigPropertiesBuilder.testApplicationConfigProperties;
import static org.junit.jupiter.api.Assertions.*;

class ActivitiesServiceImplTest {

    @Test//TODO: finalise test
    void shouldHarvestActivitiesAndSaveInParquet() throws StravaActivitiesResponseException, IOException {
        final ApplicationConfigProperties configProperties = testApplicationConfigProperties().setProperties();
        ActivitiesServiceImpl activitiesService = new ActivitiesServiceImpl(new StravaRestClient(new RestTemplate(), configProperties),
                new CustomParquetWriter(), new ActivityFactory());
        activitiesService.harvestActivities("", 1,
                "");
    }

}