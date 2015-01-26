/**
 * Robonews.io
 *
 * Copyright (c) 2013-2015 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.console.dto.channel;

import io.robonews.domain.Channel;

public class ChannelDatatableItem {

    private int id;

    private String canonicalName;

    private String title;

    private String url;

    private int scale;

    private boolean active;

    private long feedsCount;

    public static ChannelDatatableItem fromChannel(Channel channel) {
        ChannelDatatableItem item = new ChannelDatatableItem();

        item.setUrl(channel.getUrl());
        item.setId(channel.getId());
        item.setTitle(channel.getTitle());
        item.setCanonicalName(channel.getCanonicalName());
        item.setScale(channel.getScale().getId());
        item.setActive(channel.isActive());

        return item;
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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public long getFeedsCount() {
        return feedsCount;
    }

    public void setFeedsCount(long feedsCount) {
        this.feedsCount = feedsCount;
    }
}
