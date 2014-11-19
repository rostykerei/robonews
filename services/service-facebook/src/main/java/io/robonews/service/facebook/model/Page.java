/**
 * Robonews.io
 *
 * Copyright (c) 2013-2014 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.service.facebook.model;

import com.restfb.Facebook;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Page {

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
        // TODO optimize and test it
        Pattern p = Pattern.compile("\"url\"\\s*:\\s*\"(.+)\"");
        Matcher m = p.matcher(pictureUrl);

        if(m.find(1)) {
            return m.group(1);
        }

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
