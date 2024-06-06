package com.rizzutih.stravaharvester.web.strava.restclient.builders;

import com.rizzutih.stravaharvester.model.Activity;
import com.rizzutih.stravaharvester.web.response.strava.SportType;
import uk.org.fyodor.generators.Generator;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static uk.org.fyodor.generators.RDG.*;
import static uk.org.fyodor.range.Range.closed;

public class TestActivityBuilder {

    private String name = string().next();

    private Double distance = doubleVal(closed(1.0, 300.0)).next();

    private Double totalElevationGain = doubleVal(closed(10.0, 1000.0)).next();

    private SportType sportType = sportType().next();

    private Instant startDate = Instant.now();

    private Double averageSpeed = doubleVal(closed(6.0, 20.0)).next();

    private Double maxSpeed = doubleVal(closed(6.0, 20.0)).next();

    private Integer averageCadence = integer(closed(160, 200)).next();

    private Integer averageTemp = integer(closed(1, 50)).next();

    private String movingTime = "1h 7m";

    private Double movingTimeInSeconds = doubleVal(closed(10.0, 25.0)).next();;

    private String pace = integer(closed(1, 8)).next() + ":" + integer(closed(0, 59)).next();

    private Double paceInSeconds = doubleVal(closed(3.0, 12.0)).next();

    private TestActivityBuilder() {
    }

    public static TestActivityBuilder testActivityBuilder() {
        return new TestActivityBuilder();
    }

    public Generator<SportType> sportType() {
        final List<String> sportTypeValues = Arrays.stream(SportType.values())
                .map(SportType::getValue)
                .collect(Collectors.toList());

        String sportType = sportTypeValues.get(new Random().nextInt(sportTypeValues.size())).toUpperCase();
        return () -> SportType.valueOf(sportType);
    }

    public Activity build() {
        return Activity.builder()
                .name(name)
                .distance(distance)
                .totalElevationGain(totalElevationGain)
                .sportType(sportType)
                .startDate(startDate)
                .averageSpeed(averageSpeed)
                .maxSpeed(maxSpeed)
                .averageCadence(averageCadence)
                .averageTemp(averageTemp)
                .movingTime(movingTime)
                .movingTimeInSeconds(movingTimeInSeconds)
                .pace(pace)
                .paceInSeconds(paceInSeconds)
                .distanceUnit("Kilometers")
                .elevationUnit("Meters")
                .build();
    }
}
