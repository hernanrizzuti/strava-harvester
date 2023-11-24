package com.rizzutih.stravaharvester.web.strava.restclient.builders;

import com.rizzutih.stravaharvester.model.Activity;
import com.rizzutih.stravaharvester.web.response.strava.SportType;
import uk.org.fyodor.generators.Generator;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static uk.org.fyodor.generators.RDG.*;

public class TestActivityBuilder {

    private String name = string().next();

    private Double distance = doubleVal().next();

    private Double totalElevationGain = doubleVal().next();

    private SportType sportType = sportType().next();

    private Instant startDate = Instant.now();

    private Double averageSpeed = doubleVal().next();

    private Double maxSpeed = doubleVal().next();

    private Double averageCadence = doubleVal().next();

    private Integer averageTemp = integer().next();

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
                .build();
    }
}
