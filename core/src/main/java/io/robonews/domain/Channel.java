/**
 * Robonews.io
 *
 * Copyright (c) 2013-2015 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "channel", uniqueConstraints = @UniqueConstraint(columnNames = "canonicalName"))
public class Channel {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(targetEntity = Country.class, fetch = FetchType.EAGER, optional = true)
    @JoinColumn(name = "countryId", referencedColumnName = "id", nullable = true)
    private Country country;

    @ManyToOne(targetEntity = State.class, fetch = FetchType.EAGER, optional = true)
    @JoinColumn(name = "stateId", referencedColumnName = "id", nullable = true)
    private State state;

    @NotNull
    @Column(name = "canonicalName", unique = true, nullable = false, length = 255)
    private String canonicalName;

    @NotNull
    @Column(name = "title", unique = true, nullable = false, length = 255)
    private String title;

    @NotNull
    @Column(name = "url", unique = false, nullable = false, length = 255)
    private String url;

    @NotNull
    @Column(name = "scale", unique = false, nullable = false)
    private int scale;

    @Column(name = "facebookId", unique = false, nullable = true, length = 255)
    private String facebookId;

    @Column(name = "twitterId", unique = false, nullable = true, length = 255)
    private String twitterId;

    @Column(name = "googlePlusId", unique = false, nullable = true, length = 255)
    private String googlePlusId;

    @Column(name = "alexaRank")
    private int alexaRank;

    @Column(name = "description", unique = false, nullable = true, length = 255)
    private String description;

    @Column(name = "active", nullable = false)
    private boolean active;

    @Version
    @Column(name = "version")
    private long version;

    @OneToOne(fetch = FetchType.LAZY, targetEntity = ChannelImage.class, mappedBy = "channel", cascade = CascadeType.ALL)
    private ChannelImage image;

    @OneToMany(fetch = FetchType.LAZY, targetEntity = Feed.class, mappedBy = "channel")
    private Collection<Feed> feeds;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
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

    public Scale getScale() {
        return Scale.getScale(scale);
    }

    public void setScale(Scale scale) {
        this.scale = scale.getId();
    }

    public String getFacebookId() {
        return facebookId;
    }

    public void setFacebookId(String facebookId) {
        this.facebookId = facebookId;
    }

    public String getTwitterId() {
        return twitterId;
    }

    public void setTwitterId(String twitterId) {
        this.twitterId = twitterId;
    }

    public String getGooglePlusId() {
        return googlePlusId;
    }

    public void setGooglePlusId(String googlePlusId) {
        this.googlePlusId = googlePlusId;
    }

    public int getAlexaRank() {
        return alexaRank;
    }

    public void setAlexaRank(int alexaRank) {
        this.alexaRank = alexaRank;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public ChannelImage getImage() {
        return image;
    }

    public Collection<Feed> getFeeds() {
        return feeds;
    }

    public void setImage(ChannelImage picture) {
        this.image = picture;
    }

    public static enum Scale {
        GLOBAL(3),
        REGIONAL(2),
        LOCAL(1);

        private int id;

        private static final Map<Integer, Scale> lookup = new HashMap<Integer, Scale>();

        static {
            for (Scale item : Scale.values()) {
                lookup.put(item.getId(), item);
            }
        }

        private Scale(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }

        public static Scale getScale(int id) {
            return lookup.get(id);
        }
    }
}
