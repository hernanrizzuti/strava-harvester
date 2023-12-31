package com.rizzutih.stravaharvester.model;

import com.rizzutih.stravaharvester.web.response.strava.SportType;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.Instant;
import java.time.LocalDateTime;

@Getter
@Builder
@ToString
public class Activity {

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

    private String pace;

    private String distanceUnit;

    private String elevationUnit;
}
