/**
 * Robonews.io
 *
 * Copyright (c) 2013-2014 Rosty Kerei.
 * All rights reserved.
 */
package nl.rostykerei.news.messaging.domain;

public class TagMessage {

    private long storyId = 0;

    private String[] foundKeywords = {};

    public long getStoryId() {
        return storyId;
    }

    public void setStoryId(long id) {
        this.storyId = id;
    }

    public String[] getFoundKeywords() {
        return foundKeywords;
    }

    public void setFoundKeywords(String[] foundKeywords) {
        this.foundKeywords = foundKeywords;
    }
}
