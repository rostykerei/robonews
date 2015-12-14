/**
 * Robonews.io
 *
 * Copyright (c) 2013-2015 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.solr.domain;

import org.apache.solr.client.solrj.beans.Field;

import java.util.Date;

public class StoryDocument {

    @Field
    private long id;

    @Field
    private String uid;

    @Field
    private String title;

    @Field
    private String author;

    @Field
    private String link;

    @Field("isVideo")
    private boolean video;

    @Field
    private Date publicationDate;

    @Field
    private Date adjustedPublicationDate;

    @Field
    private Date createdDate;

    @Field
    private String text;

    @Field
    private String channel;

    @Field
    private long originalFeedId;

    @Field
    private long areaId;

    @Field
    private long topicId;

    @Field
    private String[] tags;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public boolean isVideo() {
        return video;
    }

    public void setVideo(boolean video) {
        this.video = video;
    }

    public Date getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(Date publicationDate) {
        this.publicationDate = publicationDate;
    }

    public Date getAdjustedPublicationDate() {
        return adjustedPublicationDate;
    }

    public void setAdjustedPublicationDate(Date adjustedPublicationDate) {
        this.adjustedPublicationDate = adjustedPublicationDate;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public long getOriginalFeedId() {
        return originalFeedId;
    }

    public void setOriginalFeedId(long originalFeedId) {
        this.originalFeedId = originalFeedId;
    }

    public long getAreaId() {
        return areaId;
    }

    public void setAreaId(long areaId) {
        this.areaId = areaId;
    }

    public long getTopicId() {
        return topicId;
    }

    public void setTopicId(long topicId) {
        this.topicId = topicId;
    }

    public String[] getTags() {
        return tags;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }
}
