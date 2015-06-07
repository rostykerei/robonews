/**
 * Robonews.io
 *
 * Copyright (c) 2013-2015 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.worker.dispatcher;

import io.robonews.dao.FeedDao;
import io.robonews.domain.Feed;
import io.robonews.messaging.domain.CrawlMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
@EnableScheduling
public class Dispatcher {

    @Value("${dispatcher.crawlInterval}")
    private int crawlInterval;

    @Value("${dispatcher.resetInterval}")
    private int resetInterval;

    @Value("${dispatcher.feedsToCrawl}")
    private int feedsToCrawl;

    @Value("${dispatcher.maxProcessTime}")
    private long maxProcessTime;

    @Autowired
    private FeedDao feedDao;

    @Autowired
    @Qualifier("crawlMessagingTemplate")
    private RabbitTemplate crawlMessaging;

    private Logger logger = LoggerFactory.getLogger(Dispatcher.class);


    @PostConstruct
    public void logProps() {
        logger.info("Dispatcher started");
        logger.info("Crawl interval [dispatcher.crawlInterval]: " + crawlInterval + "ms");
        logger.info("Feeds to crawl num [dispatcher.feedsToCrawl]: " + feedsToCrawl);
        logger.info("Reset feeds interval [dispatcher.resetInterval]: " + resetInterval + "ms");
        logger.info("Max processing time [dispatcher.maxProcessTime]: " + maxProcessTime + "ms");
    }

    @Scheduled(fixedDelayString = "${dispatcher.crawlInterval}")
    public void dispatchCrawl() {
        List<Feed> feedList = feedDao.pollFeedsToProcess(10);

        for (Feed feed : feedList) {
            CrawlMessage message = new CrawlMessage(feed.getId());
            crawlMessaging.convertAndSend(message);
        }
    }

    @Scheduled(fixedDelayString = "${dispatcher.resetInterval}")
    public void resetStuckFeeds() {
        int num = feedDao.resetIdleFeeds(maxProcessTime);
        if (num > 0) {
            logger.warn(num + " feed" + (num == 1 ? " has" : "s have") + " been stuck and reset" );
        }
    }
}
