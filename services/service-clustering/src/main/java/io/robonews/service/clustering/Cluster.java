/**
 * Robonews.io
 *
 * Copyright (c) 2013-2015 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.service.clustering;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by rosty on 04/08/15.
 */
public class Cluster {

    private String name;

    private Double score;

    private Set<Document> documents = new HashSet<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public Set<Document> getDocuments() {
        return documents;
    }
}
