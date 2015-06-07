/**
 * Robonews.io
 *
 * Copyright (c) 2013-2015 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.console.dto.feed;

/**
 * Created by rosty on 05/06/15.
 */
public class FeedDatatableItem {

    private int id;

    private int channelId;

    private String channelCN;

    private String name;

    private String url;

    private boolean video;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getChannelId() {
        return channelId;
    }

    public void setChannelId(int channelId) {
        this.channelId = channelId;
    }

    public String getChannelCN() {
        return channelCN;
    }

    public void setChannelCN(String channelCN) {
        this.channelCN = channelCN;
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

    public boolean isVideo() {
        return video;
    }

    public void setVideo(boolean video) {
        this.video = video;
    }
}
