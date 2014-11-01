/**
 * Robonews.io
 *
 * Copyright (c) 2013-2014 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.console.dto;

import io.robonews.domain.Channel;

public class ChannelDatatableItem {

    private int id;

    private String canonicalName;

    private String title;

    private String url;

    public static ChannelDatatableItem fromChannel(Channel channel) {
        ChannelDatatableItem item = new ChannelDatatableItem();

        item.setUrl(channel.getUrl());
        item.setId(channel.getId());
        item.setTitle(channel.getTitle());

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
}
