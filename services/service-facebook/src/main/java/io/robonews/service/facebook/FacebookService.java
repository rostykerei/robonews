/**
 * Robonews.io
 *
 * Copyright (c) 2013-2015 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.service.facebook;

import io.robonews.service.facebook.model.FacebookProfile;

import java.util.List;

/**
 * Facebook service interface
 *
 * @author Rosty Kerei
 */
public interface FacebookService {

    /**
     * Searches for user profiles. Return's up to 10 results.
     *
     * @param query search query
     * @return list of profiles
     */
    List<FacebookProfile> searchProfiles(String query);

    /**
     * Searches for user profiles.
     *
     * @param query search query
     * @param limit max number of results to return
     * @return list of profiles
     */
    List<FacebookProfile> searchProfiles(String query, int limit);

}
