/**
 * Robonews.io
 *
 * Copyright (c) 2013-2015 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.console.dto.feed;

import io.robonews.console.dto.validator.Url;
import io.robonews.domain.Feed;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FeedForm {

    private static Format DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    private int id;

    @Min(1)
    private int channelId;

    private String channelCN;

    @Min(1)
    private int areaId;

    @Min(1)
    private int topicId;

    @Url
    private String url;

    @Size(min = 1, max = 255)
    private String name;

    @Url
    @Size(max = 255)
    private String link;

    @Size(max = 255)
    private String description;

    @Size(max = 255)
    private String author;

    @Size(max = 255)
    private String copyright;

    @Url
    private String imageUrl;

    private boolean video = false;

    private Date lastCheck;

    private Date plannedCheck;

    private double velocity;

    @Min(0)
    @Max(60)
    private double minVelocityThreshold;

    @Min(0)
    @Max(60)
    private double maxVelocityThreshold;

    public static FeedForm fromFeed(Feed feed) {
        FeedForm form = new FeedForm();

        form.setId(feed.getId());

        form.setChannelId(feed.getChannel() != null ? feed.getChannel().getId() : 0);
        form.setAreaId(feed.getArea() != null ? feed.getArea().getId() : 0);
        form.setTopicId(feed.getTopic() != null ? feed.getTopic().getId() : 0);

        form.setUrl(feed.getUrl());
        form.setName(feed.getName());
        form.setLink(feed.getLink());
        form.setDescription(feed.getDescription());
        form.setAuthor(feed.getAuthor());
        form.setCopyright(feed.getCopyright());
        form.setImageUrl(feed.getImageUrl());
        form.setVideo(feed.isVideo());

        form.setLastCheck(feed.getLastCheck());
        form.setPlannedCheck(feed.getPlannedCheck());

        form.setMinVelocityThreshold(feed.getMinVelocityThreshold());
        form.setMaxVelocityThreshold(feed.getMaxVelocityThreshold());

        return form;
    }

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

    public int getAreaId() {
        return areaId;
    }

    public void setAreaId(int areaId) {
        this.areaId = areaId;
    }

    public int getTopicId() {
        return topicId;
    }

    public void setTopicId(int topicId) {
        this.topicId = topicId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCopyright() {
        return copyright;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isVideo() {
        return video;
    }

    public void setVideo(boolean video) {
        this.video = video;
    }

    public Date getLastCheck() {
        return lastCheck;
    }

    public void setLastCheck(Date lastCheck) {
        this.lastCheck = lastCheck;
    }

    public Date getPlannedCheck() {
        return plannedCheck;
    }

    public void setPlannedCheck(Date plannedCheck) {
        this.plannedCheck = plannedCheck;
    }

    public double getVelocity() {
        return velocity;
    }

    public void setVelocity(double velocity) {
        this.velocity = velocity;
    }

    public double getMinVelocityThreshold() {
        return minVelocityThreshold;
    }

    public void setMinVelocityThreshold(double minVelocityThreshold) {
        this.minVelocityThreshold = minVelocityThreshold;
    }

    public double getMaxVelocityThreshold() {
        return maxVelocityThreshold;
    }

    public void setMaxVelocityThreshold(double maxVelocityThreshold) {
        this.maxVelocityThreshold = maxVelocityThreshold;
    }
}
