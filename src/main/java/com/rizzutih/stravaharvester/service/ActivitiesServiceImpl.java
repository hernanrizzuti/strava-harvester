package com.rizzutih.stravaharvester.service;

import com.rizzutih.stravaharvester.client.StravaRestClient;
import com.rizzutih.stravaharvester.config.ApplicationConfigProperties;
import com.rizzutih.stravaharvester.exception.StravaActivitiesResponseException;
import com.rizzutih.stravaharvester.factory.ActivityFactory;
import com.rizzutih.stravaharvester.model.Activity;
import com.rizzutih.stravaharvester.web.response.strava.ActivityResponse;
import com.rizzutih.stravaharvester.writer.CustomParquetWriter;
import org.apache.hadoop.fs.Path;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ActivitiesServiceImpl implements ActivitiesService {

    private final StravaRestClient stravaRestClient;

    private final CustomParquetWriter customParquetWriter;

    private final ActivityFactory activityFactory;

    public ActivitiesServiceImpl(final StravaRestClient stravaRestClient,
                                 final CustomParquetWriter customParquetWriter,
                                 final ActivityFactory activityFactory) {
        this.stravaRestClient = stravaRestClient;
        this.customParquetWriter = customParquetWriter;
        this.activityFactory = activityFactory;
    }

    @Override
    public void harvestActivities(final String accessToken,
                                  final int activityYears,
                                  final String destination) throws StravaActivitiesResponseException, IOException {

        final ZonedDateTime now = ZonedDateTime.now();
        final ZonedDateTime yearsAgo = now.minusYears(activityYears);
        int pageNumber = 0;
        final int activitiesPerPage = 156;
        final long epochNow = now.toInstant().getEpochSecond();
        final long epochYearsAgo = yearsAgo.toInstant().getEpochSecond();
        List<List<ActivityResponse>> allStravaActivities = new ArrayList<>();

        while (true) {
            pageNumber++;
            final ResponseEntity<List<ActivityResponse>> response = stravaRestClient.getActivities(accessToken,
                    epochNow, epochYearsAgo, pageNumber, activitiesPerPage);
            if (response.getStatusCodeValue() != 200) {
                throw new StravaActivitiesResponseException("Strava response failure.");
            }

            final List<ActivityResponse> stravaActivities = response.getBody();
            if (stravaActivities.isEmpty()) {
                break;
            }
            allStravaActivities.add(stravaActivities);
        }
        final List<Activity> activities = activityFactory.getInstance(allStravaActivities);

        customParquetWriter.write(activities, "activity_schema.avsc", new Path(destination));

    }
}
