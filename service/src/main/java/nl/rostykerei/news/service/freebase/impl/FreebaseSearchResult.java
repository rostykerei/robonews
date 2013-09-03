package nl.rostykerei.news.service.freebase.impl;

import nl.rostykerei.news.service.core.NamedEntityType;

public class FreebaseSearchResult {

    private String mid;

    private String name;

    private float score;

    private NamedEntityType type;

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public NamedEntityType getType() {
        return type;
    }

    public void setType(NamedEntityType type) {
        this.type = type;
    }
}
