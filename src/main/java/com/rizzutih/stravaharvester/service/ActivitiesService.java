package com.rizzutih.stravaharvester.service;

import com.rizzutih.stravaharvester.exception.StravaResponseException;
import com.rizzutih.stravaharvester.model.Argument;

import java.io.IOException;

public interface ActivitiesService {

    void harvestActivities(final Argument argument) throws StravaResponseException, IOException;
}
