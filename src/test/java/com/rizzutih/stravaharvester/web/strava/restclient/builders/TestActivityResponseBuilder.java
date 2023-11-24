package com.rizzutih.stravaharvester.web.strava.restclient.builders;

import com.rizzutih.stravaharvester.web.response.strava.ActivityResponse;
import com.rizzutih.stravaharvester.web.response.strava.SportType;

import java.time.Instant;

public class TestActivityResponseBuilder {

    private TestActivityResponseBuilder() {
    }

    public static TestActivityResponseBuilder testActivityResponseBuilder() {
        return new TestActivityResponseBuilder();
    }

    public ActivityResponse build() {
        return new ActivityResponse("Afternoon Run", 7297.6, 2298,
                51.0, 78.6, 49.0, SportType.RUN, null,
                Instant.now(), 3.176, 4.484, 90.5, 17);
    }

}
