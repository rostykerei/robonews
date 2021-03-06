/**
 * Robonews.io
 *
 * Copyright (c) 2013-2015 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.service.nlp.impl;

public class NamedEntity {

    private Type type;
    private String name;
    private float termFrequency;

    public NamedEntity(Type type, String name) {
        this(type, name, 0f);
    }

    public NamedEntity(Type type, String name, float termFrequency) {
        this.type = type;
        this.name = name;
        this.termFrequency = termFrequency;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getTermFrequency() {
        return termFrequency;
    }

    public void setTermFrequency(float termFrequency) {
        this.termFrequency = termFrequency;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NamedEntity that = (NamedEntity) o;

        if (name != null ? !name.equalsIgnoreCase(that.name) : that.name != null) return false;
        if (type != that.type) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = type != null ? type.hashCode() : 0;
        result = 31 * result + (name != null ? name.toLowerCase().hashCode() : 0);
        return result;
    }

    public enum Type {
        PERSON,
        LOCATION,
        ORGANIZATION,
        MISC;
    }
}
