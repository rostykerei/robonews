/**
 * Robonews.io
 *
 * Copyright (c) 2013-2015 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.console.dto.channel;

import io.robonews.console.dto.validator.Domain;
import io.robonews.console.dto.validator.Url;
import io.robonews.domain.Channel;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

public class ChannelForm {

    private int id;

    @Domain
    @Size(min = 1, max = 255)
    private String canonicalName;

    @Size(min = 1, max = 255)
    private String title;

    @Url
    @Size(min = 6, max = 255)
    private String url;

    @Size(max = 2)
    private String country;

    @Size(max = 8)
    private String state;

    @Min(1)
    @Max(3)
    private int scale;

    @Size(max = 255)
    private String facebookId;

    @Size(max = 255)
    private String twitterId;

    @Size(max = 255)
    private String googlePlusId;

    @Min(1)
    private Integer alexaRank;

    @Size(max = 255)
    private String description;

    private boolean active;

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
        form.setActive(channel.isActive());

        if (channel.getCountry() != null) {
            form.setCountry(channel.getCountry().getIsoCode2());
        }

        if (channel.getState() != null) {
            form.setState(channel.getState().getIsoCode());
        }

        return form;
    }

    public Channel toChannel() {
        return updateChannel(new Channel());
    }

    public Channel updateChannel(Channel channel) {
        channel.setId(getId());
        channel.setCanonicalName(getCanonicalName());
        channel.setTitle(getTitle());
        channel.setUrl(getUrl());
        channel.setScale(Channel.Scale.getScale(getScale()));
        channel.setFacebookId(getFacebookId());
        channel.setTwitterId(getTwitterId());
        channel.setGooglePlusId(getGooglePlusId());
        channel.setAlexaRank(getAlexaRank());
        channel.setDescription(getDescription());
        channel.setActive(isActive());

        return channel;
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

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
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

    public Integer getAlexaRank() {
        return alexaRank;
    }

    public void setAlexaRank(Integer alexaRank) {
        this.alexaRank = alexaRank;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
