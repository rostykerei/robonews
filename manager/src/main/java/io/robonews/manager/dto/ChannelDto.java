/**
 * Robonews.io
 *
 * Copyright (c) 2013-2015 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.manager.dto;

import io.robonews.domain.Channel;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class ChannelDto {

    private int id;

    @NotNull
    @Size(min = 1, max = 255, message = "Name is required and has to be less than 255 characters long")
    private String name;

    @NotNull
    @Size(min = 1, max = 255, message = "URL is required and has to be less than 255 characters long")
    private String url;

    @Size(max = 255, message = "Description has to be less than 255 characters long")
    private String description;

    public ChannelDto() {
        super();
    }

    public ChannelDto(Channel channel) {
        setId(channel.getId());
        setName(channel.getTitle());
        setUrl(channel.getUrl());
        setDescription(channel.getDescription());
    }

    public Channel toChannel() {
        Channel channel = new Channel();
        channel.setId(getId());
        channel.setTitle(getName());
        channel.setUrl(getUrl());
        channel.setDescription(getDescription());

        return channel;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
