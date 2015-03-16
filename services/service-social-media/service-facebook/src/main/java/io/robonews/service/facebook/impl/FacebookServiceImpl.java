/**
 * Robonews.io
 *
 * Copyright (c) 2013-2015 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.service.facebook.impl;

import com.restfb.Connection;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import io.robonews.service.facebook.FacebookService;
import io.robonews.service.facebook.model.FacebookProfile;

import java.util.List;

public class FacebookServiceImpl implements FacebookService {

    private static final String PROFILE_FIELDS = "username,name,picture{url},category,website,likes,is_verified";

    private FacebookClient fbClient;

    public FacebookServiceImpl(String accessToken) {
        fbClient = new DefaultFacebookClient(accessToken);
    }


    @Override
    public List<FacebookProfile> searchProfiles(String query) {
        return searchProfiles(query, 10);
    }

    @Override
    public List<FacebookProfile> searchProfiles(String query, int limit) {

        Connection<FacebookProfile> search =
            fbClient.fetchConnection("search",
                FacebookProfile.class,
                Parameter.with("q", query),
                Parameter.with("type", "page"),
                Parameter.with("fields", PROFILE_FIELDS),
                Parameter.with("limit", limit));

        return search.getData();
    }

    @Override
    public FacebookProfile getProfile(String id) {
        return null;
    }

    @Override
    public String getProfilePictureUrl(String id) {
        try {
            FacebookProfile profile = fbClient.fetchObject(
                id,
                FacebookProfile.class,
                Parameter.with("fields", PROFILE_FIELDS)
            );

            if (profile != null && profile.getPictureUrl() != null ) {
                return profile.getPictureUrl();
            }
        }
        catch (RuntimeException e) {

        }

        return null;
    }
}
