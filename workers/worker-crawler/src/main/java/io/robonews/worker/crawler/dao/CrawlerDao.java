/**
 * Robonews.io
 *
 * Copyright (c) 2013-2015 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.worker.crawler.dao;

import io.robonews.domain.Feed;
import io.robonews.domain.Story;
import io.robonews.service.syndication.SyndicationEntry;

import java.util.Date;

public interface CrawlerDao {

    /**
     * Creates new Story or returns null if already exists
     *
     * @param syndEntry
     * @return
     */
    Story createStory(SyndicationEntry syndEntry, Feed feed, Date checkTime);

}
