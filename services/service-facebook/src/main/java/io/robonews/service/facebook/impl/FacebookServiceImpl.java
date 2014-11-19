/**
 * Robonews.io
 *
 * Copyright (c) 2013-2014 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.service.facebook.impl;

import com.restfb.Connection;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import io.robonews.service.facebook.FacebookService;
import io.robonews.service.facebook.model.Page;

import java.util.List;

public class FacebookServiceImpl implements FacebookService {

    private FacebookClient fbClient;

    public FacebookServiceImpl(String accessToken) {
        fbClient = new DefaultFacebookClient(accessToken);
    }

    private void obtainAccessToken() {
        //FacebookClient.AccessToken at = fbClient.obtainAppAccessToken("639969386124860", "a1ae6cf5e4c1f71e054f4e3843793b89");
        //System.out.println(at);
    }

    @Override
    public List<Page> searchPage(String query) {
        return searchPage(query, 10);
    }

    @Override
    public List<Page> searchPage(String query, int limit) {

        obtainAccessToken();

        Connection<Page> search =
            fbClient.fetchConnection("search",
                Page.class,
                Parameter.with("q", query),
                Parameter.with("type", "page"),
                Parameter.with("fields", "username,name,picture{url},category,website,likes,is_verified"),
                Parameter.with("limit", limit));

        return search.getData();
    }
}
