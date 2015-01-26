/**
 * Robonews.io
 *
 * Copyright (c) 2013-2015 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.manager.dto;

import io.robonews.domain.Feed;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.util.Date;

public class FeedDto {

    private int id;

    @Min(value = 1, message = "Channel cannot be empty")
    private int channelId;

    @Min(value = 1, message = "Category cannot be empty")
    private int categoryId;

    @Size(min = 1, max = 255, message = "URL is required and has to be less than 255 characters long")
    private String url;

    @Size(min = 1, max = 255, message = "Name is required and has to be less than 255 characters long")
    private String name;

    @Size(min = 1, max = 255, message = "Link is required and has to be less than 255 characters long")
    private String link;

    @Size(max = 255, message = "Description has to be less than 255 characters long")
    private String description;

    @Size(max = 255, message = "Author has to be less than 255 characters long")
    private String author;

    @Size(max = 255, message = "Copyright has to be less than 255 characters long")
    private String copyright;

    @Size(max = 255, message = "Image URL has to be less than 255 characters long")
    private String imageUrl;

    private boolean video;

    private Date lastCheck;

    private Date plannedCheck;

    private Date inProgressSince;

    @Min(value = 1/24, message = "Velocity cannot be less than 1/24 (once a day)")
    @Max(value = 120, message = "Velocity cannot be more than 120 (twice a minute)")
    private double velocity;

    @Min(value = 1/24, message = "Velocity cannot be less than 1/24 (once a day)")
    @Max(value = 120, message = "Velocity cannot be more than 120 (twice a minute)")
    private double minVelocityThreshold;

    @Min(value = 1/24, message = "Velocity cannot be less than 1/24 (once a day)")
    @Max(value = 120, message = "Velocity cannot be more than 120 (twice a minute)")
    private double maxVelocityThreshold;

    @Size(max = 255, message = "HTTP ETag has to be less than 255 characters long")
    private String httpLastEtag;

    private Date httpLastModified;

    public FeedDto() {
        super();
    }

    public FeedDto(Feed feed) {
        setId(feed.getId());
        setChannelId(feed.getChannel().getId());
        setCategoryId(feed.getCategory().getId());
        setUrl(feed.getUrl());
        setName(feed.getName());
        setLink(feed.getLink());
        setDescription(feed.getDescription());
        setAuthor(feed.getAuthor());
        setCopyright(feed.getCopyright());
        setImageUrl(feed.getImageUrl());
        setVideo(feed.isVideo());
        setLastCheck(feed.getLastCheck());
        setPlannedCheck(feed.getPlannedCheck());
        setInProgressSince(feed.getInProcessSince());
        setVelocity(feed.getVelocity());
        setMinVelocityThreshold(feed.getMinVelocityThreshold());
        setMaxVelocityThreshold(feed.getMaxVelocityThreshold());
        setHttpLastEtag(feed.getHttpLastEtag());
        setHttpLastModified(feed.getHttpLastModified());
    }

    public Feed toFeed() {
        Feed feed = new Feed();
        feed.setId(getId());
        feed.setUrl(getUrl());
        feed.setName(getName());
        feed.setLink(getLink());
        feed.setDescription(getDescription());
        feed.setAuthor(getAuthor());
        feed.setCopyright(getCopyright());
        feed.setImageUrl(getImageUrl());
        feed.setVideo(isVideo());
        feed.setLastCheck(getLastCheck());
        feed.setPlannedCheck(getPlannedCheck());
        feed.setInProcessSince(getInProgressSince());
        feed.setVelocity(getVelocity());
        feed.setMinVelocityThreshold(getMinVelocityThreshold());
        feed.setMaxVelocityThreshold(getMaxVelocityThreshold());
        feed.setHttpLastEtag(getHttpLastEtag());
        feed.setHttpLastModified(getHttpLastModified());

        return feed;
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

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
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

    public Date getInProgressSince() {
        return inProgressSince;
    }

    public void setInProgressSince(Date inProgressSince) {
        this.inProgressSince = inProgressSince;
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

    public String getHttpLastEtag() {
        return httpLastEtag;
    }

    public void setHttpLastEtag(String httpLastEtag) {
        this.httpLastEtag = httpLastEtag;
    }

    public Date getHttpLastModified() {
        return httpLastModified;
    }

    public void setHttpLastModified(Date httpLastModified) {
        this.httpLastModified = httpLastModified;
    }
}
