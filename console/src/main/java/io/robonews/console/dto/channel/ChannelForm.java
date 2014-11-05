/**
 * Robonews.io
 *
 * Copyright (c) 2013-2014 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.console.dto.channel;

import io.robonews.domain.Channel;

public class ChannelForm {

    private int id;

    private String canonicalName;

    private String title;

    private String url;

    private int scale;

    private String facebookId;

    private String twitterId;

    private String googlePlusId;

    private int alexaRank;

    private String description;

    public static ChannelForm fromChannel(Channel channel) {
        ChannelForm form = new ChannelForm();

        form.setId(channel.getId());
        form.setCanonicalName(channel.getCanonicalName());
        form.setTitle(channel.getTitle());
        form.setUrl(channel.getUrl());
        form.setScale(channel.getScale().getId());
        form.setFacebookId(channel.getFacebookId());
        form.setTwitterId(channel.getTwitterId());
        form.setGooglePlusId(channel.getGooglePlusId());
        form.setAlexaRank(channel.getAlexaRank());
        form.setDescription(channel.getDescription());

        return form;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCanonicalName() {
        return canonicalName;
    }

    public void setCanonicalName(String canonicalName) {
        this.canonicalName = canonicalName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getScale() {
        return scale;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }

    public String getFacebookId() {
        return facebookId;
    }

    public void setFacebookId(String facebookId) {
        this.facebookId = facebookId;
    }

    public String getTwitterId() {
        return twitterId;
    }

    public void setTwitterId(String twitterId) {
        this.twitterId = twitterId;
    }

    public String getGooglePlusId() {
        return googlePlusId;
    }

    public void setGooglePlusId(String googlePlusId) {
        this.googlePlusId = googlePlusId;
    }

    public int getAlexaRank() {
        return alexaRank;
    }

    public void setAlexaRank(int alexaRank) {
        this.alexaRank = alexaRank;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
