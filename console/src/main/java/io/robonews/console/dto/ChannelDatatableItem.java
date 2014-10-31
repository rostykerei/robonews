package io.robonews.console.dto;

import io.robonews.domain.Channel;

public class ChannelDatatableItem {

    private int id;

    private String name;

    private String url;

    public static ChannelDatatableItem fromChannel(Channel channel) {
        ChannelDatatableItem item = new ChannelDatatableItem();

        item.setUrl(channel.getUrl());
        item.setId(channel.getId());
        item.setName(channel.getName());

        return item;
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
}
