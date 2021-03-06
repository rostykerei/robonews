/**
 * Robonews.io
 *
 * Copyright (c) 2013-2015 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.domain;


import javax.persistence.*;

@Entity
@Table(name = "tag_alternative", uniqueConstraints = @UniqueConstraint(columnNames = {"name"}))
public class TagAlternative {

    @Id
    @Column(name = "id")
    @GeneratedValue
    private long id;

    @Column(name = "typeId")
    private int type;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "confidence")
    private float confidence;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tagId", nullable = false)
    private Tag tag;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Tag.Type getType() {
        return Tag.Type.getType(type);
    }

    public void setType(Tag.Type type) {
        this.type = type.getId();
    }

    public float getConfidence() {
        return confidence;
    }

    public void setConfidence(float confidence) {
        this.confidence = confidence;
    }

    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }
}
