/**
 * Robonews.io
 *
 * Copyright (c) 2013-2015 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.domain;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "feed", uniqueConstraints = @UniqueConstraint(columnNames = "url"))
public class Feed {

    @Id
    @Column(name = "id")
    @GeneratedValue
    private int id;

    @ManyToOne
    @JoinColumn(name = "channelId", referencedColumnName = "id", nullable = false)
    private Channel channel;

    @ManyToOne
    @JoinColumn(name = "areaId", referencedColumnName = "id", nullable = false)
    private Area area;

    @ManyToOne
    @JoinColumn(name = "topicId", referencedColumnName = "id", nullable = false)
    private Topic topic;

    @Column(name = "url", unique = true, nullable = false, length = 255)
    private String url;

    @Column(name = "name")
    private String name;

    @Column(name = "link")
    private String link;

    @Column(name = "description")
    private String description;

    @Column(name = "author")
    private String author;

    @Column(name = "copyright")
    private String copyright;

    @Column(name = "imageUrl")
    private String imageUrl;

    @Column(name = "isVideo")
    private boolean video;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "lastCheck")
    private Date lastCheck;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "plannedCheck")
    private Date plannedCheck;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "inProcessSince")
    private Date inProcessSince;

    @Column(name = "velocity")
    private double velocity;

    @Column(name = "minVelocityThreshold")
    private double minVelocityThreshold;

    @Column(name = "maxVelocityThreshold")
    private double maxVelocityThreshold;

    @Column(name = "httpLastEtag")
    private String httpLastEtag;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "httpLastModified")
    private Date httpLastModified;

    public String toString() {
        return "Feed: [id: " + getId() + ", name: " + getName() + "]";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
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

    public void setName(String title) {
        this.name = title;
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

    public Date getInProcessSince() {
        return inProcessSince;
    }

    public void setInProcessSince(Date inProgressSince) {
        this.inProcessSince = inProgressSince;
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
