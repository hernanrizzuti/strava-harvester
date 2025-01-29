package com.rizzutih.stravaharvester.service;

import com.rizzutih.stravaharvester.exception.StravaResponseException;

import java.io.IOException;

public interface ActivitiesService {

    void harvestActivities(final String accessToken,
                           final int yearsOfActivities,
                           final String activityDestination) throws StravaResponseException, IOException;
}
