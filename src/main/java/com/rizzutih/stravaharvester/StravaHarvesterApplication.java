package com.rizzutih.stravaharvester;

import com.rizzutih.stravaharvester.client.StravaRestClient;
import com.rizzutih.stravaharvester.config.ApplicationConfigProperties;
import com.rizzutih.stravaharvester.exception.StravaActivitiesResponseException;
import com.rizzutih.stravaharvester.factory.ActivityFactory;
import com.rizzutih.stravaharvester.service.ActivitiesServiceImpl;
import com.rizzutih.stravaharvester.writer.CustomParquetWriter;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@SpringBootApplication
public class StravaHarvesterApplication implements ApplicationRunner {

    final StravaRestClient stravaRestClient;
    final ActivitiesServiceImpl activitiesService;

    public StravaHarvesterApplication(final StravaRestClient stravaRestClient,
                                      final ActivitiesServiceImpl activitiesService) {
        this.stravaRestClient = stravaRestClient;
        this.activitiesService = activitiesService;
    }

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(StravaHarvesterApplication.class);
        application.setAddCommandLineProperties(false);
        application.run(args);
    }

    @Override
    public void run(ApplicationArguments args) throws StravaActivitiesResponseException, IOException {

        activitiesService.harvestActivities("", 0, "");

    }
}
