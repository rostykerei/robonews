/**
 * Robonews.io
 *
 * Copyright (c) 2013-2015 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.service.socialmedia;

import java.util.List;

/**
 * Common social-media service interface
 *
 * @author Rosty Kerei
 */
public interface SocialMediaService<T extends SocialMediaProfile> {

    /**
     * Search for a list of 10 profiles
     *
     * @param query Query string
     * @return List of 10 profiles
     */
    List<T> searchProfiles(String query);

    /**
     * Search for a list of {@param limit} profiles
     *
     * @param query Query string
     * @param limit Maxim number of profiles to return
     * @return List of profiles
     */
    List<T> searchProfiles(String query, int limit);

    /**
     * Get social-media profile by id
     *
     * @param id ID
     * @return Social-media profile
     */
    T getProfile(String id);

    /**
     * Get profiles picture URL
     *
     * @param id Profile ID
     * @return Picture URL string or null if not found
     */
    String getProfilePictureUrl(String id);

}
