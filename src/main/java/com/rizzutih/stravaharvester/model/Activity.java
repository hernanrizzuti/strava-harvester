package com.rizzutih.stravaharvester.model;

import com.rizzutih.stravaharvester.web.response.strava.SportType;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.Instant;

@Getter
@Builder
@ToString
public class Activity {

    private Long athleteStravaId;

    private String name;

    private Double distance;

    private Double totalElevationGain;

    private SportType sportType;

    private Instant startDate;

    private Double averageSpeed;

    private Double maxSpeed;

    private Integer averageCadence;

    private Integer averageTemp;

    private String movingTime;

    private double movingTimeInSeconds;

    private String pace;

    private Double paceInSeconds;

    private String distanceUnit;

    private String elevationUnit;
}
