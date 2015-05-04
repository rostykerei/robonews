/**
 * Robonews.io
 *
 * Copyright (c) 2013-2015 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.worker.crawler.dao.impl;

import io.robonews.dao.StoryDao;
import io.robonews.domain.Feed;
import io.robonews.domain.NewsCategory;
import io.robonews.domain.Story;
import io.robonews.service.syndication.SyndicationEntry;
import io.robonews.worker.crawler.dao.CrawlerDao;
import io.robonews.worker.crawler.util.Sanitizer;
import io.robonews.worker.crawler.util.SanitizerException;
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
            story.setNewsCategory(feed.getNewsCategory());
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

        NewsCategory oldNewsCategory = oldStory.getNewsCategory();
        NewsCategory newNewsCategory = newFeed.getNewsCategory();

        boolean updated = false;

        if (oldNewsCategory.getId() != newNewsCategory.getId()) {
            if (newNewsCategory.isPriority() == oldNewsCategory.isPriority()) {
                if (newNewsCategory.getLevel() > oldNewsCategory.getLevel()) {
                    oldStory.setNewsCategory(newNewsCategory);
                    updated = true;
                }
            }
            else if (newNewsCategory.isPriority()) {
                oldStory.setNewsCategory(newNewsCategory);
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
