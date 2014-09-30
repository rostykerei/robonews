/**
 * Robonews.io
 *
 * Copyright (c) 2013-2014 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.worker.crawler.controller.impl;

import io.robonews.dao.FeedDao;
import io.robonews.domain.Feed;
import io.robonews.domain.Story;
import io.robonews.messaging.domain.ImageMessage;
import io.robonews.messaging.domain.PageMessage;
import io.robonews.messaging.domain.TagMessage;
import io.robonews.service.http.HttpRequest;
import io.robonews.service.http.HttpResponse;
import io.robonews.service.http.HttpService;
import io.robonews.service.http.impl.HttpRequestImpl;
import io.robonews.service.syndication.SyndicationEntry;
import io.robonews.service.syndication.SyndicationFeed;
import io.robonews.service.syndication.SyndicationService;
import io.robonews.service.syndication.SyndicationServiceParsingException;
import io.robonews.worker.crawler.controller.CrawlerController;
import io.robonews.worker.crawler.dao.CrawlerDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.scheduling.annotation.Async;

import java.io.IOException;
import java.util.Date;


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

    @Autowired
    @Qualifier("pageMessagingTemplate")
    private RabbitTemplate pageMessaging;

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
        Date httpExpires = null;

        try {
            httpResponse = crawl(feed.getUrl(), feed.getHttpLastEtag(), feed.getHttpLastModified());

            if (httpResponse != null) {
                httpExpires = httpResponse.getExpires();

                if (httpResponse.getHttpStatus() == HttpResponse.STATUS_OK) {
                    feed.setHttpLastEtag(httpResponse.getEtag());
                    feed.setHttpLastModified(httpResponse.getLastModified());

                    syndicationFeed = syndicationService.loadFeed(httpResponse.getStream());

                    // Very important to release HTTP connection !
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

                                // TODO
                                Date processingDeadline = new Date( new Date().getTime() + 900000L );

                                try {
                                    sendTagMessage(syndEntry, story, processingDeadline);
                                }
                                catch (RuntimeException e) {
                                    logger.warn("Cannot send tagMessage: ", e);
                                }

                                try {
                                    sendImageMessage(syndEntry, story, processingDeadline);
                                }
                                catch (RuntimeException e) {
                                    logger.warn("Cannot send imageMessage: ", e);
                                }

                                try {
                                    sendPageMessage(story, processingDeadline);
                                }
                                catch (RuntimeException e) {
                                    logger.warn("Cannot send pageMessage: ", e);
                                }
                            }
                            catch (DataIntegrityViolationException e){
                                // do nothing
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
                else if (httpResponse.getHttpStatus() == HttpResponse.STATUS_NOT_MODIFIED) {
                    // Feed is not modified
                    newStories = 0;
                }
                else {
                    // TODO - notify 404 eg
                    logger.info("Failed to crawl feed [" + feed + "], http status: " + httpResponse.getHttpStatus());
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

            // Very important to release HTTP connection !
            if (httpResponse != null) {
                httpResponse.releaseConnection();
            }

            feed.setVelocity(calculateVelocity(feed, checkTime, newStories));
            feed.setPlannedCheck( calculateNextCheck(feed, checkTime, httpExpires) );
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

        return (feed.getVelocity() + newStories) / ( ((checkTime.getTime() - lastCheck) / 3600000D) + 1);
    }

    private Date calculateNextCheck(Feed feed, Date checkTime, Date httpExpires) {
        double velocity = feed.getVelocity();

        if (velocity < feed.getMinVelocityThreshold()) {
            velocity = feed.getMinVelocityThreshold();
        }
        else if (velocity > feed.getMaxVelocityThreshold()) {
            velocity = feed.getMaxVelocityThreshold();
        }

        if (velocity > 0) {

            long nextByVelocity = (long) (checkTime.getTime() + (3600000L / velocity));

            if (httpExpires != null) {

                long nextByHttp = httpExpires.getTime();

                if (nextByVelocity <= nextByHttp || (new Date().getTime() >= nextByHttp)) {
                    return new Date( nextByVelocity );
                }
                else {
                    long nextByMaxVelocity = (long) (checkTime.getTime() + (3600000L / feed.getMaxVelocityThreshold()));

                    return new Date( Math.max(nextByHttp, nextByMaxVelocity) );
                }
            }
            else {
                return new Date( nextByVelocity );
            }
        }
        else {
            // Should never happens, but prevents division by 0
            return new Date(checkTime.getTime() + 3600000L);
        }
    }

    private void sendTagMessage(SyndicationEntry syndEntry, Story story, Date processingDeadline) {
        TagMessage message = new TagMessage();
        message.setStoryId(story.getId());
        message.setDeadline(processingDeadline);

        if (syndEntry.getMediaKeywords() != null && syndEntry.getMediaKeywords().size() > 0) {
            message.setFoundKeywords(
                syndEntry.getMediaKeywords().toArray(
                    new String[syndEntry.getMediaKeywords().size()]
                )
            );
        }

        tagMessaging.convertAndSend(message);
    }

    private void sendImageMessage(SyndicationEntry syndEntry, Story story, Date processingDeadline) {
        if (syndEntry.getMediaImages() == null || syndEntry.getMediaImages().size() < 1) {
            return;
        }

        for (String imageUrl : syndEntry.getMediaImages()) {
            ImageMessage message = new ImageMessage();
            message.setStoryId(story.getId());
            message.setImageUrl(imageUrl);
            message.setDeadline(processingDeadline);

            imageMessaging.convertAndSend(message);
        }
    }

    private void sendPageMessage(Story story, Date processingDeadline) {
        PageMessage pageMessage = new PageMessage();
        pageMessage.setStoryId(story.getId());
        pageMessage.setPageUrl(story.getLink());
        pageMessage.setDeadline(processingDeadline);

        pageMessaging.convertAndSend(pageMessage);
    }
}
