package nl.rostykerei.news.crawler;

import nl.rostykerei.news.crawler.processors.StoryPostProcessor;
import nl.rostykerei.news.crawler.processors.StoryPreProcessor;
import nl.rostykerei.news.dao.FeedDao;
import nl.rostykerei.news.dao.StoryDao;
import nl.rostykerei.news.domain.Category;
import nl.rostykerei.news.domain.Feed;
import nl.rostykerei.news.domain.Story;
import nl.rostykerei.news.service.http.HttpRequest;
import nl.rostykerei.news.service.http.HttpResponse;
import nl.rostykerei.news.service.http.HttpService;
import nl.rostykerei.news.service.http.impl.HttpRequestImpl;
import nl.rostykerei.news.service.syndication.SyndicationEntry;
import nl.rostykerei.news.service.syndication.SyndicationFeed;
import nl.rostykerei.news.service.syndication.SyndicationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.Set;

public class Crawler {

    @Autowired
    private HttpService httpService;

    @Autowired
    private SyndicationService syndicationService;

    @Autowired
    private FeedDao feedDao;

    @Autowired
    private StoryDao storyDao;

    private Set<StoryPreProcessor> preProcessors;

    private Set<StoryPostProcessor> postProcessors;

    private Logger logger = LoggerFactory.getLogger(Crawler.class);

    @Async
    public void run() {
        logger.info("Crawler starts, thread " + Thread.currentThread().getName());

        Feed feed = feedDao.pollFeedToProcess();

        if (feed != null) {
            HttpRequest httpRequest = new HttpRequestImpl(feed.getUrl());

            httpRequest.setLastEtag(feed.getHttpLastEtag());
            httpRequest.setLastModified(feed.getHttpLastModified());

            int newStories = 0;
            Date checkTime = new Date();
            HttpResponse httpResponse = null;
            SyndicationFeed syndicationFeed = null;

            try {
                httpResponse = httpService.execute(httpRequest);

                if (httpResponse.getHttpStatus() == 200) {
                    feed.setHttpLastEtag(httpResponse.getEtag());
                    feed.setHttpLastModified(httpResponse.getLastModified());

                    syndicationFeed = syndicationService.loadFeed(httpResponse.getStream());

                    if (syndicationFeed == null) {
                        throw new RuntimeException("Cannot load feed");
                    }

                    for (SyndicationEntry syndEntry : syndicationFeed.getEntries()) {
                        if (!StringUtils.isEmpty(syndEntry.getGuid())) {
                            Story oldStory = storyDao.getByGuid(feed.getChannel(), syndEntry.getGuid());

                            if (oldStory == null) {
                                // Processing new story...
                                processAsNewStory(syndEntry, feed, checkTime);
                            }
                            else {
                                // Processing existent story...
                                updateExistentStory(oldStory, feed);
                            }
                        }
                    }
                }
                else if (httpResponse.getHttpStatus() == 304) {
                    newStories = 0;
                }
                else {
                    newStories = 0;
                }
            } catch (Exception e) {
                logger.info("Failed to crawl", e);
            }
            finally {

                synchronized (this) {
                    if (httpResponse != null) {
                        httpResponse.releaseConnection();
                    }
                }

                feed.setVelocity(calculateVelocity(feed, checkTime, newStories));
                feed.setPlannedCheck(calculateNextCheck(feed, checkTime));
                feed.setLastCheck(checkTime);
                feed.setInProcessSince(null);
                feedDao.update(feed);
            }
        }

        logger.info("Crawler ends, thread " + Thread.currentThread().getName());
    }

    void processAsNewStory(SyndicationEntry syndEntry, Feed feed, Date checkTime) {
        for (StoryPreProcessor preProcessor : preProcessors) {
            syndEntry = preProcessor.preProcess(syndEntry);
        }

        Story story = new Story();
        story.setAuthor(syndEntry.getAuthor());
        story.setCategory(feed.getCategory());
        story.setChannel(feed.getChannel());
        story.setCreatedDate(checkTime);
        story.setDescription(syndEntry.getDescription());
        story.setGuid(syndEntry.getGuid());
        story.setLink(syndEntry.getLink());
        story.setOriginalFeed(feed);
        story.setPublicationDate(syndEntry.getPubDate());
        story.setTitle(syndEntry.getTitle());
        story.setVideo(feed.isVideo());

        storyDao.create(story);

        for (StoryPostProcessor postProcessor : postProcessors) {
            postProcessor.postProcess(story);
        }

        logger.info("NEWS: " + syndEntry.getTitle());
    }

    void updateExistentStory(Story oldStory, Feed newFeed) {

        Category oldCategory = oldStory.getCategory();
        Category newCategory = newFeed.getCategory();

        boolean updated = false;

        if (oldCategory.getId() != newCategory.getId()) {
            if (newCategory.isPriority() == oldCategory.isPriority()) {
                if (newCategory.getLevel() > oldCategory.getLevel()) {
                    oldStory.setCategory(newCategory);
                    updated = true;
                }
            }
            else if (newCategory.isPriority()) {
                oldStory.setCategory(newCategory);
                updated = true;
            }
        }

        if (newFeed.isVideo() && !oldStory.isVideo()) {
            oldStory.setVideo(true);
            updated = true;
        }

        if (updated) {
            storyDao.update(oldStory);
        }
    }

    double calculateVelocity(Feed feed, Date checkTime, int newStories) {
        // TODO
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

    Date calculateNextCheck(Feed feed, Date checkTime) {
        double velocity = feed.getVelocity();

        if (velocity > 0) {
            return new Date( (long) (checkTime.getTime() + (3600000L / velocity)) );
        }
        else {
            // Should never happens, but prevent division by 0
            return new Date(checkTime.getTime() + 3600000L * 24L);
        }
    }

    public Set<StoryPreProcessor> getPreProcessors() {
        return preProcessors;
    }

    public void setPreProcessors(Set<StoryPreProcessor> preProcessors) {
        this.preProcessors = preProcessors;
    }

    public Set<StoryPostProcessor> getPostProcessors() {
        return postProcessors;
    }

    public void setPostProcessors(Set<StoryPostProcessor> postProcessors) {
        this.postProcessors = postProcessors;
    }

    public static void main(String[] args) throws InterruptedException {
        System.setProperty("spring.profiles.active", "fill-db,test");
        ApplicationContext context =
                new ClassPathXmlApplicationContext("crawlerContext.xml");

        Crawler c = (Crawler) context.getBean("crawler");

        c.run();
    }
}
