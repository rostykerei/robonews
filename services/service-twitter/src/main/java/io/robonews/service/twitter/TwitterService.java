/**
 * Robonews.io
 *
 * Copyright (c) 2013-2015 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.service.twitter;

import io.robonews.service.twitter.model.TwitterProfile;
import java.util.List;

public interface TwitterService {

    /**
     * Searches for user profiles. Return's up to 10 results.
     *
     * @param query search query
     * @return list of twitter profiles
     */
    List<TwitterProfile> searchProfiles(String query);

    /**
     * Searches for user profiles.
     *
     * @param query search query
     * @param limit maximum results to return
     * @return list of profiles
     */
    List<TwitterProfile> searchProfiles(String query, int limit);
}
