package com.rizzutih.stravaharvester.factory;

import com.rizzutih.stravaharvester.model.Athlete;
import com.rizzutih.stravaharvester.web.response.strava.AthleteResponse;
import org.springframework.stereotype.Component;

@Component
public class AthleteFactory {

    public Athlete getInstance(final AthleteResponse athleteResponse) {
        return Athlete.builder()
                .stravaId(athleteResponse.getId())
                .firstname(athleteResponse.getFirstname())
                .lastname(athleteResponse.getLastname())
                .build();
    }
}
