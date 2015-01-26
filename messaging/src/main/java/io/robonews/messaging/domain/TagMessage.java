/**
 * Robonews.io
 *
 * Copyright (c) 2013-2015 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.messaging.domain;

public class TagMessage extends AbstractMessage {

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
