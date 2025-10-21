package com.rizzutih.stravaharvester.service;

import com.rizzutih.stravaharvester.client.StravaRestClient;
import com.rizzutih.stravaharvester.exception.StravaResponseException;
import com.rizzutih.stravaharvester.factory.ActivityFactory;
import com.rizzutih.stravaharvester.factory.AthleteFactory;
import com.rizzutih.stravaharvester.model.Activity;
import com.rizzutih.stravaharvester.model.Argument;
import com.rizzutih.stravaharvester.model.Athlete;
import com.rizzutih.stravaharvester.web.response.strava.ActivityResponse;
import com.rizzutih.stravaharvester.web.response.strava.AthleteResponse;
import com.rizzutih.stravaharvester.web.response.strava.SportType;
import com.rizzutih.stravaharvester.writer.CustomParquetWriter;
import org.apache.hadoop.fs.Path;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.nonNull;

@Service
public class ActivitiesServiceImpl implements ActivitiesService {

    private final StravaRestClient stravaRestClient;

    private final CustomParquetWriter customParquetWriter;

    private final ActivityFactory activityFactory;

    private final AthleteFactory athleteFactory;

    public ActivitiesServiceImpl(final StravaRestClient stravaRestClient,
                                 final CustomParquetWriter customParquetWriter,
                                 final ActivityFactory activityFactory,
                                 final AthleteFactory athleteFactory) {

        this.stravaRestClient = stravaRestClient;
        this.customParquetWriter = customParquetWriter;
        this.activityFactory = activityFactory;
        this.athleteFactory = athleteFactory;
    }

    @Override
    public void harvestActivities(final Argument argument) throws StravaResponseException, IOException {

        final String accessToken = argument.getAccessToken();
        final String activityDestination = argument.getHarvestedActivityDestination();
        final ResponseEntity<AthleteResponse> athleteResponse = stravaRestClient.getAthlete(accessToken);//TODO: put this in a separated service

        //TODO: put this in a separated service
        if (athleteResponse.getStatusCode().value() != 200) {
            throw new StravaResponseException("Strava athlete response failure.");
        }

        final ZonedDateTime now = ZonedDateTime.now();
        final ZonedDateTime yearsAgo = now.minusYears(argument.getActivityYears());
        int pageNumber = 0;
        final int activitiesPerPage = 156;
        final long epochNow = now.toInstant().getEpochSecond();
        final long epochYearsAgo = yearsAgo.toInstant().getEpochSecond();
        final List<List<ActivityResponse>> allStravaActivities = new ArrayList<>();

        while (true) {
            pageNumber++;
            final ResponseEntity<List<ActivityResponse>> activityResponse = stravaRestClient.getActivities(accessToken,
                    epochNow, epochYearsAgo, pageNumber, activitiesPerPage);

            if (activityResponse.getStatusCode().value() != 200) {
                throw new StravaResponseException("Strava activity response failure.");
            }

            final List<ActivityResponse> stravaActivities = activityResponse.getBody();
            if (nonNull(stravaActivities) && stravaActivities.isEmpty()) {
                break;
            }
            if (SportType.ALL == argument.getSportType()) {
                allStravaActivities.add(stravaActivities);
            } else {
                final List<ActivityResponse> filteredActivities = stravaActivities.stream()
                        .filter(x -> x.getSportType() == argument.getSportType())
                        .toList();
                allStravaActivities.add(filteredActivities);
            }
        }

        final AthleteResponse athleteResponseBody = athleteResponse.getBody();
        final List<Activity> activities = activityFactory.getInstance(allStravaActivities, athleteResponseBody);
        final Athlete athlete = athleteFactory.getInstance(athleteResponseBody);//TODO: put this in a separated service
        customParquetWriter.writeAthlete(athlete, "athlete_schema.avsc", new Path(String.format("%s/athlete.parquet", activityDestination)));//TODO: put this in a separated service
        customParquetWriter.writeActivity(activities, "activity_schema.avsc", new Path(String.format("%s/activities.parquet", activityDestination)));
    }
}
