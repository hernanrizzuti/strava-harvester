package com.rizzutih.stravaharvester.model;

import com.rizzutih.stravaharvester.web.response.strava.SportType;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class Argument {

    final private String accessToken;
    final private int activityYears;
    final private String harvestedActivityDestination;
    final private SportType sportType;

}
