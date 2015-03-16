/**
 * Robonews.io
 *
 * Copyright (c) 2013-2015 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.service.google.plus.model;

import io.robonews.service.socialmedia.SocialMediaProfile;

/**
 * Created by rosty on 13/02/15.
 */
public class GooglePlusProfile implements SocialMediaProfile {

    private String id;

    private String name;

    private String url;

    private String plusName;

    private String imageUrl;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPlusName() {
        return plusName;
    }

    public void setPlusName(String plusName) {
        this.plusName = plusName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
