/**
 * Robonews.io
 *
 * Copyright (c) 2013-2015 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.worker.crawler.job;

import io.robonews.dao.FeedDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class ResetIdleFeeds {

    @Autowired
    private FeedDao feedDao;

    private int rate;

    private Logger logger = LoggerFactory.getLogger(getClass());

    public ResetIdleFeeds(int rate) {
        this.rate = rate;
    }

    public void run() {
        int num = feedDao.resetIdleFeeds( rate );
        logger.info(num + " feed(s) has been reset");
    }

}
