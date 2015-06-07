/**
 * Robonews.io
 *
 * Copyright (c) 2013-2015 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.dao;

import io.robonews.domain.Feed;

import java.util.List;

public interface FeedDao extends AbstractDao<Feed, Integer> {

    List<Feed> getAll();

    Feed getByUrl(String url);

    /**
     * Returns top planned feed and sets inProgressSince = now
     *
     * @return feed
     */
    Feed pollFeedToProcess();

    void releaseFeedInProcess(Feed feed);

    int resetIdleFeeds(long busyMilliseconds);
}
