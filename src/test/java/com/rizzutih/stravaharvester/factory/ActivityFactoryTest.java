package com.rizzutih.stravaharvester.factory;

import com.rizzutih.stravaharvester.model.Activity;
import com.rizzutih.stravaharvester.web.response.strava.ActivityResponse;
import com.rizzutih.stravaharvester.web.response.strava.AthleteResponse;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static com.rizzutih.stravaharvester.web.strava.restclient.builders.TestActivityResponseBuilder.testActivityResponseBuilder;
import static com.rizzutih.stravaharvester.web.strava.restclient.builders.TestAthleteResponseBuilder.testAthleteResponseBuilder;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ActivityFactoryTest {

    @Test
    void shouldReturnActivitiesWhenStravaActivitiesArePassedIn() {
        final ActivityFactory factory = new ActivityFactory();
        final ActivityResponse activityResponse = testActivityResponseBuilder().build();
        final AthleteResponse athleteResponse = testAthleteResponseBuilder().build();

        final List<Activity> result = factory.getInstance(Arrays.asList(Arrays.asList(activityResponse)), athleteResponse);
        Activity actualActivity = result.get(0);

        assertEquals(athleteResponse.getId(), actualActivity.getAthleteStravaId());
        assertEquals(activityResponse.getName(), actualActivity.getName());
        assertEquals(activityResponse.getTotalElevationGain(), actualActivity.getTotalElevationGain());
        assertEquals(activityResponse.getSportType(), actualActivity.getSportType());
        assertEquals(activityResponse.getStartDate(), actualActivity.getStartDate());
        assertEquals(11.43, actualActivity.getAverageSpeed());
        assertEquals(activityResponse.getAverageTemp(), actualActivity.getAverageTemp());
        assertEquals(16.14, actualActivity.getMaxSpeed());
        assertEquals(181, actualActivity.getAverageCadence());
        assertEquals("00:38:18", actualActivity.getMovingTime());
        assertEquals(activityResponse.getMovingTime(), actualActivity.getMovingTimeInSeconds());
        assertEquals("05:14", actualActivity.getPace());
        assertEquals(activityResponse.getMovingTime() / (activityResponse.getDistance() / 1000),
                actualActivity.getPaceInSeconds());
        assertEquals("Kilometers", actualActivity.getDistanceUnit());
        assertEquals("Meters", actualActivity.getElevationUnit());
    }

}