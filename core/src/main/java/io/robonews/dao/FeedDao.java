/**
 * Robonews.io
 *
 * Copyright (c) 2013-2014 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.dao;

import io.robonews.domain.Feed;

import java.util.List;

public interface FeedDao extends AbstractDao<Feed, Integer> {

    List<Feed> getAll();

    /**
     * Returns top planned feed and sets inProgressSince = now
     *
     * @return feed
     */
    Feed pollFeedToProcess();

    void releaseFeedInProcess(Feed feed);
}
