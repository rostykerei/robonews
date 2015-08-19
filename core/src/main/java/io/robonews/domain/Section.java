/**
 * Robonews.io
 *
 * Copyright (c) 2013-2015 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "section", uniqueConstraints = @UniqueConstraint(columnNames = "name"))
public class Section {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    @Column(name = "name", unique = true, nullable = false, length = 255)
    private String name;

    @Column(name = "active", nullable = false)
    private boolean active;

    @ManyToMany(cascade = CascadeType.ALL, targetEntity = Feed.class, fetch = FetchType.EAGER)
    @JoinTable(name = "section_lead_feed",
            uniqueConstraints = @UniqueConstraint(columnNames = {"sectionId", "feedId"}),
            joinColumns = { @JoinColumn(name = "sectionId", nullable = false, updatable = false) },
            inverseJoinColumns = { @JoinColumn(name = "feedId", nullable = false, updatable = false) })
    private Set<Feed> leadFeeds = new HashSet<>();

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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Set<Feed> getLeadFeeds() {
        return leadFeeds;
    }
}
