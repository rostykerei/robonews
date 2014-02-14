/**
 * Robonews.io
 *
 * Copyright (c) 2013-2014 Rosty Kerei.
 * All rights reserved.
 */
package nl.rostykerei.news.worker.crawler.controller.impl;

import java.io.IOException;
import java.util.Date;
import nl.rostykerei.news.dao.FeedDao;
import nl.rostykerei.news.domain.Feed;
import nl.rostykerei.news.domain.Story;
import nl.rostykerei.news.messaging.domain.ImageMessage;
import nl.rostykerei.news.messaging.domain.TagMessage;
import nl.rostykerei.news.service.http.HttpRequest;
import nl.rostykerei.news.service.http.HttpResponse;
import nl.rostykerei.news.service.http.HttpService;
import nl.rostykerei.news.service.http.impl.HttpRequestImpl;
import nl.rostykerei.news.service.syndication.SyndicationEntry;
import nl.rostykerei.news.service.syndication.SyndicationFeed;
import nl.rostykerei.news.service.syndication.SyndicationService;
import nl.rostykerei.news.service.syndication.SyndicationServiceParsingException;
import nl.rostykerei.news.worker.crawler.controller.CrawlerController;
import nl.rostykerei.news.worker.crawler.dao.CrawlerDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.scheduling.annotation.Async;


public class CrawlerControllerImpl implements CrawlerController {

    @Autowired
    private FeedDao feedDao;

    @Autowired
    private CrawlerDao crawlerDao;

    @Autowired
    private HttpService httpService;

    @Autowired
    private SyndicationService syndicationService;

    @Autowired
    @Qualifier("tagMessagingTemplate")
    private RabbitTemplate tagMessaging;

    @Autowired
    @Qualifier("imageMessagingTemplate")
    private RabbitTemplate imageMessaging;

    private Logger logger = LoggerFactory.getLogger(CrawlerControllerImpl.class);

    @Async
    @Override
    public void execute() {
        Feed feed = feedDao.pollFeedToProcess();

        if (feed != null) {
            processFeed(feed);
        }
    }

    private void processFeed(Feed feed) {
        // Crawl feed
        HttpResponse httpResponse = null;
        SyndicationFeed syndicationFeed = null;
        int newStories = 0;
        Date checkTime = new Date();

        try {
            httpResponse = crawl(feed.getUrl(), feed.getHttpLastEtag(), feed.getHttpLastModified());

            if (httpResponse != null) {
                if (httpResponse.getHttpStatus() == 200) {
                    feed.setHttpLastEtag(httpResponse.getEtag());
                    feed.setHttpLastModified(httpResponse.getLastModified());

                    syndicationFeed = syndicationService.loadFeed(httpResponse.getStream());

                    httpResponse.releaseConnection();
                    httpResponse = null;

                    if (syndicationFeed != null) {
                        for (SyndicationEntry syndEntry : syndicationFeed.getEntries()) {
                            try {
                                Story story = crawlerDao.createStory(syndEntry, feed, checkTime);

                                if (story == null) {
                                    continue;
                                }

                                newStories++;

                                try {
                                    sendTagMessage(syndEntry, story);
                                }
                                catch (RuntimeException e) {
                                    logger.warn("Cannot send tagMessage: ", e);
                                }

                                try {
                                    sendImageMessage(syndEntry, story);
                                }
                                catch (RuntimeException e) {
                                    logger.warn("Cannot send imageMessage: ", e);
                                }
                            }
                            catch (DataIntegrityViolationException e){
                                logger.info("Duplicate story: " + syndEntry.getGuid());
                                continue;
                            }
                            catch (RuntimeException e) {
                                logger.info("Failed to create story: ", e);
                                continue;
                            }
                        }

                    }
                    else {
                        logger.info("Failed to parse feed [" + feed + "], Syndication service returned null");
                    }
                }
                else if (httpResponse.getHttpStatus() == 304) {
                    // Feed is not modified
                    newStories = 0;
                }
                else {
                    logger.info("Failed to crawl feed [" + feed + "], http status: " + httpResponse.getHttpStatus());
                    httpResponse = null;
                }
            }
            else {
                logger.info("Failed to crawl feed [" + feed + "], HTTP service returned null response");
            }
        }
        catch (SyndicationServiceParsingException e) {
            logger.info("Failed to parse feed [" + feed + "]", e);
        }
        catch (IOException e) {
            logger.info("Failed to crawl feed [" + feed + "]", e);
        }
        finally {

            if (httpResponse != null) {
                httpResponse.releaseConnection();
            }

            feed.setVelocity(calculateVelocity(feed, checkTime, newStories));
            feed.setPlannedCheck(calculateNextCheck(feed, checkTime));
            feed.setLastCheck(checkTime);
            feed.setInProcessSince(null);
            feedDao.update(feed);
        }

    }

    private HttpResponse crawl(String url, String etag, Date lastModified) throws IOException {
        HttpRequest httpRequest = new HttpRequestImpl(url);

        httpRequest.setLastEtag(etag);
        httpRequest.setLastModified(lastModified);
        httpRequest.setAccept("application/rss+xml," +
                "application/rdf+xml," +
                "application/atom+xml," +
                "application/xml,text/xml");

        return httpService.execute(httpRequest);
    }

    private double calculateVelocity(Feed feed, Date checkTime, int newStories) {
        // TODO if lastcheck is null
        long lastCheck = feed.getLastCheck() != null ? feed.getLastCheck().getTime() : new Date().getTime() - 3600000L;

        double newVelocity = (feed.getVelocity() + newStories) / ( ((checkTime.getTime() - lastCheck) / 3600000D) + 1);

        if (newVelocity < feed.getMinVelocityThreshold()) {
            newVelocity = feed.getMinVelocityThreshold();
        }
        else if (newVelocity > feed.getMaxVelocityThreshold()) {
            newVelocity = feed.getMaxVelocityThreshold();
        }

        return newVelocity;
    }

    private Date calculateNextCheck(Feed feed, Date checkTime) {
        double velocity = feed.getVelocity();

        if (velocity > 0) {
            return new Date( (long) (checkTime.getTime() + (3600000L / velocity)) );
        }
        else {
            // Should never happens, but prevents division by 0
            return new Date(checkTime.getTime() + 3600000L * 24L);
        }
    }

    private void sendTagMessage(SyndicationEntry syndEntry, Story story) {
        TagMessage message = new TagMessage();
        message.setStoryId(story.getId());

        if (syndEntry.getMediaKeywords() != null && syndEntry.getMediaKeywords().size() > 0) {
            message.setFoundKeywords(
                    syndEntry.getMediaKeywords().toArray(
                            new String[syndEntry.getMediaKeywords().size()]
                    )
            );
        }

        tagMessaging.convertAndSend(message);
    }

    private void sendImageMessage(SyndicationEntry syndEntry, Story story) {
        if (syndEntry.getMediaImages() == null || syndEntry.getMediaImages().size() < 1) {
            return;
        }

        for (String imageUrl : syndEntry.getMediaImages()) {
            ImageMessage message = new ImageMessage();
            message.setStoryId(story.getId());
            message.setImageUrl(imageUrl);

            imageMessaging.convertAndSend(message);
        }
    }
}
