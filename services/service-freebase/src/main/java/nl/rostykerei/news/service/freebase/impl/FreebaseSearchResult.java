package nl.rostykerei.news.service.freebase.impl;

public class FreebaseSearchResult {

    private String mid;

    private String name;

    private float score;

    private Type type;

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

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public enum Type {
        PERSON,
        LOCATION,
        ORGANIZATION,
        MISC;
    }

}
