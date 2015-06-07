/**
 * Robonews.io
 *
 * Copyright (c) 2013-2015 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.messaging.domain;

/**
 * Created by rosty on 07/06/15.
 */
public class CrawlMessage {

    private int feedId;

    public CrawlMessage(int feedId) {
        this.feedId = feedId;
    }

    public int getFeedId() {
        return feedId;
    }
}
