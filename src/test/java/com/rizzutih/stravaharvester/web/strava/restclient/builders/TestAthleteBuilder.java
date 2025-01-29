package com.rizzutih.stravaharvester.web.strava.restclient.builders;

import com.rizzutih.stravaharvester.model.Athlete;

import static uk.org.fyodor.generators.RDG.longVal;
import static uk.org.fyodor.generators.RDG.string;

public class TestAthleteBuilder {

    private long id = longVal().next();

    private String firstName = string().next();

    private String lastName = string().next();

    private TestAthleteBuilder() {
    }

    public static TestAthleteBuilder testAthleteBuilder() {
        return new TestAthleteBuilder();
    }

    public Athlete build(){
        return Athlete.builder().stravaId(id).firstname(firstName).lastname(lastName).build();
    }

}
