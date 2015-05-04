/**
 * Robonews.io
 *
 * Copyright (c) 2013-2015 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.worker.crawler.dao;

import io.robonews.dao.StoryDao;
import io.robonews.domain.Channel;
import io.robonews.domain.Feed;
import io.robonews.domain.NewsCategory;
import io.robonews.domain.Story;
import io.robonews.service.syndication.SyndicationEntry;
import io.robonews.service.syndication.impl.SyndicationEntryImpl;
import io.robonews.worker.crawler.dao.impl.CrawlerDaoImpl;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Date;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CrawlerDaoTest {

    @Mock
    private StoryDao storyDao;

    @InjectMocks
    private CrawlerDao crawlerDao = new CrawlerDaoImpl();

    @Test
    public void testCreateValidStory() throws Exception {
        SyndicationEntry syndEntry = new SyndicationEntryImpl();
        syndEntry.setTitle("story-title");
        syndEntry.setLink("http://www.robonews.io/index.html");
        syndEntry.setGuid("test-unique-guid");
        syndEntry.setPubDate(new Date());
        syndEntry.setDescription("story-description");

        Feed feed = new Feed();
        feed.setName("feed-name");

        Story story = crawlerDao.createStory(syndEntry, feed, new Date());

        verify(storyDao).create(any(Story.class));

        Assert.assertNotNull(story);
        Assert.assertEquals("test-unique-guid", story.getGuid());
    }

    @Test
    public void testCreateInvalidStory() {
        SyndicationEntry syndEntry = new SyndicationEntryImpl();
        syndEntry.setTitle("story-title");
        syndEntry.setLink("broken.link");
        syndEntry.setGuid("test-unique-guid");
        syndEntry.setPubDate(new Date());
        syndEntry.setDescription("story-description");

        Feed feed = new Feed();
        feed.setName("feed-name");

        Story story = crawlerDao.createStory(syndEntry, feed, new Date());

        verify(storyDao, times(0)).create(any(Story.class));

        Assert.assertNull(story);
    }

    @Test
    public void testCreateExistentPrioStory() {
        SyndicationEntry syndEntry = new SyndicationEntryImpl();
        syndEntry.setTitle("story-title");
        syndEntry.setLink("http://www.robonews.io/index.html");
        syndEntry.setGuid("test-unique-guid");
        syndEntry.setPubDate(new Date());
        syndEntry.setDescription("story-description");

        NewsCategory oldNewsCategory = new NewsCategory();
        oldNewsCategory.setId(1);
        oldNewsCategory.setLevel(10);
        oldNewsCategory.setPriority(true);

        NewsCategory newNewsCategory = new NewsCategory();
        newNewsCategory.setId(2);
        newNewsCategory.setLevel(20);
        newNewsCategory.setPriority(true);

        Feed newFeed = new Feed();
        newFeed.setNewsCategory(newNewsCategory);
        newFeed.setVideo(true);

        Story story = new Story();
        story.setNewsCategory(oldNewsCategory);
        story.setVideo(false);

        when(storyDao.getByGuid(any(Channel.class), eq("test-unique-guid"))).thenReturn(story);

        Story storyResult = crawlerDao.createStory(syndEntry, newFeed, new Date());

        verify(storyDao).update(story);
        verify(storyDao, times(0)).create(any(Story.class));

        Assert.assertTrue(story.isVideo());
        Assert.assertEquals(newNewsCategory.getId(), story.getNewsCategory().getId());
    }

    @Test
    public void testCreateExistentSamePrioLevelStory() {
        SyndicationEntry syndEntry = new SyndicationEntryImpl();
        syndEntry.setTitle("story-title");
        syndEntry.setLink("http://www.robonews.io/index.html");
        syndEntry.setGuid("test-unique-guid");
        syndEntry.setPubDate(new Date());
        syndEntry.setDescription("story-description");

        NewsCategory oldNewsCategory = new NewsCategory();
        oldNewsCategory.setId(1);
        oldNewsCategory.setLevel(10);
        oldNewsCategory.setPriority(false);

        NewsCategory newNewsCategory = new NewsCategory();
        newNewsCategory.setId(2);
        newNewsCategory.setLevel(5);
        newNewsCategory.setPriority(true);

        Feed newFeed = new Feed();
        newFeed.setNewsCategory(newNewsCategory);
        newFeed.setVideo(true);

        Story story = new Story();
        story.setNewsCategory(oldNewsCategory);
        story.setVideo(false);

        when(storyDao.getByGuid(any(Channel.class), eq("test-unique-guid"))).thenReturn(story);

        Story storyResult = crawlerDao.createStory(syndEntry, newFeed, new Date());

        verify(storyDao).update(story);
        verify(storyDao, times(0)).create(any(Story.class));

        Assert.assertTrue(story.isVideo());
        Assert.assertEquals(newNewsCategory.getId(), story.getNewsCategory().getId());
    }

}
