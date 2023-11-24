package com.rizzutih.stravaharvester.factory;

import com.rizzutih.stravaharvester.model.Activity;
import com.rizzutih.stravaharvester.web.response.strava.ActivityResponse;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ActivityFactory {

    public List<Activity> getInstance(List<List<ActivityResponse>> stravaActivities) {

        //TODO: Create an Utility class to separate factory from all conventions
        List<Activity> activities = new ArrayList<>();
        List<ActivityResponse> allStravaActivities = stravaActivities.stream()
                .flatMap(List::stream)
                .collect(Collectors.toList());

        allStravaActivities.stream().forEach(x -> {
            //Original distance unit is meters. Divided by 1000 to convert to km then rounded by two decimal places.
            double distanceInKM = x.getDistance() / 1000;

            final double distance = new BigDecimal(distanceInKM)
                    .setScale(2, RoundingMode.HALF_UP)
                    .doubleValue();

            final double averageSpeed = new BigDecimal(x.getAverageSpeed() * 3.6)
                    .setScale(2, RoundingMode.HALF_UP)
                    .doubleValue();//convert it from meters to km/h

            final double maxSpeed = new BigDecimal(x.getMaxSpeed() * 3.6)
                    .setScale(2, RoundingMode.HALF_UP)
                    .doubleValue();//convert it from meters to km/h

            final int averageCadence = new BigDecimal(x.getAverageCadence() * 2)
                    .setScale(0, RoundingMode.HALF_UP)
                    .intValue();

            final int movingTimeInSeconds = x.getMovingTime();
            final String movingTime = getMovingTime(movingTimeInSeconds);

            String pace = getPace(distanceInKM, movingTimeInSeconds);

            final Activity activity = Activity.builder()
                    .name(x.getName())
                    .distance(distance)
                    .totalElevationGain(x.getTotalElevationGain())
                    .sportType(x.getSportType())
                    .startDate(x.getStartDate())
                    .averageSpeed(averageSpeed)
                    .maxSpeed(maxSpeed)
                    .averageCadence(averageCadence)
                    .averageTemp(x.getAverageTemp())
                    .movingTime(movingTime)
                    .pace(pace)
                    .distanceUnit("Kilometers")
                    .elevationUnit("Meters")
                    .build();

            activities.add(activity);
        });


        return activities;
    }

    private String getPace(double distanceInKM, int movingTimeInSeconds) {
        double paceInSeconds = movingTimeInSeconds / distanceInKM;

        final int minutes = (int) (paceInSeconds % 3600) / 60;
        final int seconds = (int) paceInSeconds % 60;

        String pace = String.format("%02d:%02d", minutes, seconds);
        return pace;
    }

    private String getMovingTime(int movingTimeInSec) {
        final int hours = movingTimeInSec / 3600;
        final int minutes = (movingTimeInSec % 3600) / 60;
        final int seconds = movingTimeInSec % 60;

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
}
