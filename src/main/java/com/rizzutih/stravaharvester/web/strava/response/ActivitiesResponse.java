package com.rizzutih.stravaharvester.web.strava.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class ActivitiesResponse {

    private String name;
    private float distance;
    private int moving_time;
    private float total_elevation_gain;
    private float elev_high;
    private float elev_low;
    private SportType sport_type;
    private int workout_type;
    private LocalDateTime start_date;
    private float average_speed;
    private float max_speed;
    private float average_cadence;
    private int average_temp;

}
