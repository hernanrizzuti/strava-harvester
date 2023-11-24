package com.rizzutih.stravaharvester.web.response.strava;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ActivityResponse {

    private String name;
    private double distance;
    @JsonProperty("moving_time")
    private int movingTime;
    @JsonProperty("total_elevation_gain")
    private double totalElevationGain;
    @JsonProperty("elev_high")
    private double elevHigh;
    @JsonProperty("elev_low")
    private double elevLow;
    @JsonProperty("sport_type")
    private SportType sportType;
    @JsonProperty("workout_type")
    private Integer workoutType;
    @JsonProperty("start_date")
    private Instant startDate;
    @JsonProperty("average_speed")
    private double averageSpeed;
    @JsonProperty("max_speed")
    private double maxSpeed;
    @JsonProperty("average_cadence")
    private double averageCadence;
    @JsonProperty("average_temp")
    private int averageTemp;

}
