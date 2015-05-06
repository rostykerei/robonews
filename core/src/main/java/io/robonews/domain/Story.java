/**
 * Robonews.io
 *
 * Copyright (c) 2013-2015 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.domain;

import io.robonews.util.KeyGenerator;
import org.apache.commons.codec.digest.DigestUtils;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "story",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"uid"}),
        @UniqueConstraint(columnNames = {"channelId", "guidHash"}),
        @UniqueConstraint(columnNames = {"channelId", "contentHash"}),
    })
public class Story {

    @Id
    @Column(name = "id")
    @GeneratedValue
    private long id;

    @Column(name = "uid", length = 11, nullable = false, unique = true)
    @GeneratedValue
    private String uid = KeyGenerator.generateKey();

    @NotNull
    @ManyToOne
    @JoinColumn(name = "channelId", referencedColumnName = "id", nullable = false)
    private Channel channel;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "areaId", referencedColumnName = "id", nullable = false)
    private Area area;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "topicId", referencedColumnName = "id", nullable = false)
    private Topic topic;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "originalFeedId", referencedColumnName = "id", nullable = false)
    private Feed originalFeed;

    @Column(name = "guidHash", unique = false, nullable = false, length = 20)
    private byte[] guidHash;

    @Column(name = "contentHash", unique = false, nullable = false, length = 20)
    private byte[] contentHash;

    @Column(name = "title", unique = false, nullable = false, length = 255)
    private String title;

    @Column(name = "author", unique = false, nullable = true, length = 255)
    private String author;

    @Column(name = "link", unique = false, nullable = false, length = 255)
    private String link;

    @Column(name = "guid", unique = false, nullable = false, length = 255)
    private String guid;

    @Column(name = "isVideo")
    private boolean video;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "publicationDate", unique = false, nullable = false)
    private Date publicationDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "createdDate", unique = false, nullable = false)
    private Date createdDate;

    @Column(name = "description", unique = false, nullable = false)
    private String description;

    @ManyToMany(cascade = CascadeType.ALL, targetEntity = Tag.class)
    @JoinTable(name = "story_tag",
            uniqueConstraints = @UniqueConstraint(columnNames = {"tagId", "storyId"}),
            joinColumns = {
            @JoinColumn(name = "storyId", nullable = false, updatable = false) },
            inverseJoinColumns = { @JoinColumn(name = "tagId",
                    nullable = false, updatable = false) })
    private Set<Tag> tags = new HashSet<Tag>();

    public static final int MAXIMUM_ALLOWED_TAGS = 32;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
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

    public Feed getOriginalFeed() {
        return originalFeed;
    }

    public void setOriginalFeed(Feed originalFeed) {
        this.originalFeed = originalFeed;
    }

    public byte[] getGuidHash() {
        return guidHash;
    }

    public byte[] getContentHash() {
        return contentHash;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        updateContentHash();
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

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
        this.guidHash = DigestUtils.sha1(guid);
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

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
        updateContentHash();
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> namedEntities) {
        this.tags = namedEntities;
    }

    private void updateContentHash() {
        this.contentHash = DigestUtils.sha1(
            new StringBuilder().append(getTitle()).append(getDescription()).toString()
        );
    }

}
