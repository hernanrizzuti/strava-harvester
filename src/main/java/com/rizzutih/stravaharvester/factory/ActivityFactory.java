package com.rizzutih.stravaharvester.factory;

import com.rizzutih.stravaharvester.model.Activity;
import com.rizzutih.stravaharvester.web.response.strava.ActivityResponse;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ActivityFactory {

    public List<Activity> getInstance(List<List<ActivityResponse>> stravaActivities) {

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
                    .movingTimeInSeconds(x.getMovingTime())
                    .pace(getPace(distanceInKM, movingTimeInSeconds))
                    .paceInSeconds(getPaceInSeconds(distanceInKM, movingTimeInSeconds))
                    .distanceUnit("Kilometers")
                    .elevationUnit("Meters")
                    .build();

            activities.add(activity);
        });


        return activities;
    }

    private String getPace(double distanceInKM, int movingTimeInSeconds) {
        double paceInSeconds = getPaceInSeconds(distanceInKM, movingTimeInSeconds);

        final int minutes = (int) (paceInSeconds % 3600) / 60;
        final int seconds = (int) paceInSeconds % 60;

        String pace = String.format("%02d:%02d", minutes, seconds);
        return pace;
    }

    double getPaceInSeconds(double distanceInKM,
                            int movingTimeInSeconds) {
        return movingTimeInSeconds / distanceInKM;
    }

    private String getMovingTime(int movingTimeInSec) {
        final int hours = movingTimeInSec / 3600;
        final int minutes = (movingTimeInSec % 3600) / 60;
        final int seconds = movingTimeInSec % 60;

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
}
