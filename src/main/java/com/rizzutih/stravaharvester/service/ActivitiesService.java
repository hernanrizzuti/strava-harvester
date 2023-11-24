package com.rizzutih.stravaharvester.service;

import com.rizzutih.stravaharvester.exception.StravaActivitiesResponseException;

import java.io.IOException;

public interface ActivitiesService {

    void harvestActivities(final String accessToken,
                           final int yearsOfActivities,
                           final String destination) throws StravaActivitiesResponseException, IOException;
}
