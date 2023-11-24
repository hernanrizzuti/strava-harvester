package com.rizzutih.stravaharvester.web.response.strava;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class ActivityResponse {

    private String name;
    private float distance;
    @JsonProperty("moving_time")
    private int movingTime;
    @JsonProperty("total_elevation_gain")
    private float totalElevationGain;
    @JsonProperty("elev_high")
    private float elevHigh;
    @JsonProperty("elev_low")
    private float elevLow;
    @JsonProperty("sport_type")
    private SportType sportType;
    @JsonProperty("workout_type")
    private int workoutType;
    @JsonProperty("start_date")
    private Instant startDate;
    @JsonProperty("average_speed")
    private float averageSpeed;
    @JsonProperty("max_speed")
    private float maxSpeed;
    @JsonProperty("average_cadence")
    private float averageCadence;
    @JsonProperty("average_temp")
    private int averageTemp;

}
