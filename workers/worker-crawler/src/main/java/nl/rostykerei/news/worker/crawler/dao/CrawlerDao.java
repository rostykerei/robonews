/**
 * Robonews.io
 *
 * Copyright (c) 2013-2014 Rosty Kerei.
 * All rights reserved.
 */
package nl.rostykerei.news.worker.crawler.dao;

import nl.rostykerei.news.domain.Feed;
import nl.rostykerei.news.domain.Story;
import nl.rostykerei.news.service.syndication.SyndicationEntry;

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
