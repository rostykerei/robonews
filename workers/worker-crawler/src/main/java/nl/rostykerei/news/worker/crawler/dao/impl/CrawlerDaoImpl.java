/**
 * Robonews.io
 *
 * Copyright (c) 2013-2014 Rosty Kerei.
 * All rights reserved.
 */
package nl.rostykerei.news.worker.crawler.dao.impl;

import nl.rostykerei.news.worker.crawler.dao.CrawlerDao;
import nl.rostykerei.news.worker.crawler.util.Sanitizer;
import nl.rostykerei.news.dao.StoryDao;
import nl.rostykerei.news.domain.Category;
import nl.rostykerei.news.domain.Feed;
import nl.rostykerei.news.domain.Story;
import nl.rostykerei.news.service.syndication.SyndicationEntry;
import nl.rostykerei.news.worker.crawler.util.SanitizerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

public class CrawlerDaoImpl implements CrawlerDao {

    @Autowired
    private StoryDao storyDao;

    @Override
    @Transactional
    public Story createStory(SyndicationEntry syndEntry, Feed feed, Date checkTime) {
        Story oldStory = storyDao.getByGuid(feed.getChannel(), syndEntry.getGuid());

        if (oldStory != null) {
            updateExistentStory(oldStory, feed);
            return null;
        }
        else {
            try {
                syndEntry = Sanitizer.sanitize(syndEntry);
            }
            catch (SanitizerException e) {
                return null;
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
}
