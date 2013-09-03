package nl.rostykerei.news.crawler.controller.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Set;
import nl.rostykerei.news.crawler.controller.CrawlerController;
import nl.rostykerei.news.crawler.dao.CrawlerDao;
import nl.rostykerei.news.crawler.processors.StoryPostProcessor;
import nl.rostykerei.news.crawler.processors.StoryPreProcessor;
import nl.rostykerei.news.dao.FeedDao;
import nl.rostykerei.news.domain.Feed;
import nl.rostykerei.news.domain.Story;
import nl.rostykerei.news.service.http.HttpRequest;
import nl.rostykerei.news.service.http.HttpResponse;
import nl.rostykerei.news.service.http.HttpService;
import nl.rostykerei.news.service.http.impl.HttpRequestImpl;
import nl.rostykerei.news.service.syndication.SyndicationEntry;
import nl.rostykerei.news.service.syndication.SyndicationFeed;
import nl.rostykerei.news.service.syndication.SyndicationService;
import nl.rostykerei.news.service.syndication.SyndicationServiceParsingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    private Logger logger = LoggerFactory.getLogger(CrawlerControllerImpl.class);

    private Set<StoryPostProcessor> postProcessors;

    @Async
    @Override
    public void execute() {
        System.out.println("Thread starts: " + Thread.currentThread().getName());
        /*try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }                                                                     */


        Feed feed = feedDao.pollFeedToProcess();

        if (feed != null) {
            processFeed(feed);
        }
        System.out.println("Thread end: " + Thread.currentThread().getName());
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

                    for (SyndicationEntry syndEntry : syndicationFeed.getEntries()) {

                        Story story = null;

                        try {
                            story = crawlerDao.createStory(syndEntry, feed, checkTime);

                            if (story == null) {
                                continue;
                            }

                            newStories++;
                        }
                        catch (RuntimeException e) {
                            logger.info("Failed to create story: ", e);
                            continue;
                        }

                        for (StoryPostProcessor postProcessor : getPostProcessors()) {
                            try {
                                postProcessor.postProcess(story);
                            }
                            catch (RuntimeException e) {
                                logger.info("Failed to post process story: ", e);
                                continue;
                            }
                        }
                    }
                }
                else if (httpResponse.getHttpStatus() == 304) {
                    // Feed is not modified
                    newStories = 0;
                    httpResponse.releaseConnection();
                }
                else {
                    logger.info("Failed to crawl feed [" + feed + "], http status: " + httpResponse.getHttpStatus());
                    httpResponse.releaseConnection();
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
            // Should never happens, but prevent division by 0
            return new Date(checkTime.getTime() + 3600000L * 24L);
        }
    }

    public Set<StoryPostProcessor> getPostProcessors() {
        return postProcessors;
    }

    public void setPostProcessors(Set<StoryPostProcessor> postProcessors) {
        this.postProcessors = postProcessors;
    }
}
