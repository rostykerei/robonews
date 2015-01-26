/**
 * Robonews.io
 *
 * Copyright (c) 2013-2015 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.service.facebook.model;

import com.restfb.Facebook;
import com.restfb.JsonMapper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FacebookProfile {

    private static final Pattern PICTURE_URL_REGEX_PATTERN = Pattern.compile("\"url\"\\s*:\\s*\"(http.+)\"");

    @Facebook("username")
    private String username;

    @Facebook("name")
    private String name;

    @Facebook("picture")
    private String pictureUrl;

    @Facebook("category")
    private String category;

    @Facebook("website")
    private String website;

    @Facebook("likes")
    private long likes;

    @Facebook("is_verified")
    private boolean verified;

    @JsonMapper.JsonMappingCompleted
    private void allDone(JsonMapper jsonMapper) {
        Matcher m = PICTURE_URL_REGEX_PATTERN.matcher(pictureUrl);

        if(m.find(1)) {
            pictureUrl = m.group(1);
        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public long getLikes() {
        return likes;
    }

    public void setLikes(long likes) {
        this.likes = likes;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }
}
