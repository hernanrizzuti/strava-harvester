package com.rizzutih.stravaharvester.factory;

import com.rizzutih.stravaharvester.model.Athlete;
import com.rizzutih.stravaharvester.web.response.strava.AthleteResponse;
import org.junit.jupiter.api.Test;

import static com.rizzutih.stravaharvester.web.strava.restclient.builders.TestAthleteResponseBuilder.testAthleteResponseBuilder;
import static org.junit.jupiter.api.Assertions.*;

class AthleteFactoryTest {

    @Test
    void shouldReturnAthlete(){
        final AthleteResponse athleteResponse = testAthleteResponseBuilder().build();
        final AthleteFactory athleteFactory = new AthleteFactory();
        final Athlete athlete = athleteFactory.getInstance(athleteResponse);

        assertEquals(athleteResponse.getId(), athlete.getStravaId());
        assertEquals(athleteResponse.getFirstname(), athlete.getFirstname());
        assertEquals(athleteResponse.getLastname(), athlete.getLastname());
    }

}