/**
 * Robonews.io
 *
 * Copyright (c) 2013-2014 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.domain;

import org.codehaus.jackson.annotate.JsonIgnore;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "channel", uniqueConstraints = @UniqueConstraint(columnNames = "name"))
public class Channel {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name", unique = true, nullable = false, length = 255)
    private String name;

    @Column(name = "url", unique = false, nullable = false, length = 255)
    private String url;

    @Column(name = "description", unique = false, nullable = true, length = 255)
    private String description;

    @Version
    @Column(name = "version")
    private long version;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, targetEntity = Feed.class, mappedBy = "channel")
    private Collection<Feed> feeds;

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

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public Collection<Feed> getFeeds() {
        return feeds;
    }

    public void setFeeds(Collection<Feed> feeds) {
        this.feeds = feeds;
    }
}
