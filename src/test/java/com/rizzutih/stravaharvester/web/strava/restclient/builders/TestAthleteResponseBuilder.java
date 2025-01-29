package com.rizzutih.stravaharvester.web.strava.restclient.builders;

import com.rizzutih.stravaharvester.web.response.strava.ActivityResponse;
import com.rizzutih.stravaharvester.web.response.strava.AthleteResponse;
import com.rizzutih.stravaharvester.web.response.strava.SportType;

import java.time.Instant;

import static uk.org.fyodor.generators.RDG.longVal;
import static uk.org.fyodor.generators.RDG.string;

public class TestAthleteResponseBuilder {

    private long id = longVal().next();

    private String firstName = string().next();

    private String lastName = string().next();


    private TestAthleteResponseBuilder() {
    }

    public static TestAthleteResponseBuilder testAthleteResponseBuilder() {
        return new TestAthleteResponseBuilder();
    }

    public AthleteResponse build() {
        return new AthleteResponse(id,firstName,lastName);
    }
}
