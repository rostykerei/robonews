package nl.rostykerei.news.crawler.dao.impl;

import java.util.Date;
import java.util.Set;
import nl.rostykerei.news.crawler.dao.CrawlerDao;
import nl.rostykerei.news.crawler.processors.StoryPreProcessor;
import nl.rostykerei.news.dao.StoryDao;
import nl.rostykerei.news.domain.Category;
import nl.rostykerei.news.domain.Feed;
import nl.rostykerei.news.domain.Story;
import nl.rostykerei.news.service.syndication.SyndicationEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

public class CrawlerDaoImpl implements CrawlerDao {

    @Autowired
    private StoryDao storyDao;

    private Set<StoryPreProcessor> preProcessors;

    @Override
    @Transactional
    public Story createStory(SyndicationEntry syndEntry, Feed feed, Date checkTime) {
        Story oldStory = storyDao.getByGuid(feed.getChannel(), syndEntry.getGuid());

        if (oldStory != null) {
            updateExistentStory(oldStory, feed);
            return null;
        }
        else {
            for (StoryPreProcessor preProcessor : getPreProcessors()) {
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

            return story;
        }
    }

    private void updateExistentStory(Story oldStory, Feed newFeed) {

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

    public Set<StoryPreProcessor> getPreProcessors() {
        return preProcessors;
    }

    public void setPreProcessors(Set<StoryPreProcessor> preProcessors) {
        this.preProcessors = preProcessors;
    }
}
