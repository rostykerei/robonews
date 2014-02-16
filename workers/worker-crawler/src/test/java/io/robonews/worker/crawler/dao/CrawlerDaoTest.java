/**
 * Robonews.io
 *
 * Copyright (c) 2013-2014 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.worker.crawler.dao;

import io.robonews.dao.StoryDao;
import io.robonews.domain.Category;
import io.robonews.domain.Channel;
import io.robonews.domain.Feed;
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

        Category oldCategory = new Category();
        oldCategory.setId(1);
        oldCategory.setLevel(10);
        oldCategory.setPriority(true);

        Category newCategory = new Category();
        newCategory.setId(2);
        newCategory.setLevel(20);
        newCategory.setPriority(true);

        Feed newFeed = new Feed();
        newFeed.setCategory(newCategory);
        newFeed.setVideo(true);

        Story story = new Story();
        story.setCategory(oldCategory);
        story.setVideo(false);

        when(storyDao.getByGuid(any(Channel.class), eq("test-unique-guid"))).thenReturn(story);

        Story storyResult = crawlerDao.createStory(syndEntry, newFeed, new Date());

        verify(storyDao).update(story);
        verify(storyDao, times(0)).create(any(Story.class));

        Assert.assertTrue(story.isVideo());
        Assert.assertEquals(newCategory.getId(), story.getCategory().getId());
    }

    @Test
    public void testCreateExistentSamePrioLevelStory() {
        SyndicationEntry syndEntry = new SyndicationEntryImpl();
        syndEntry.setTitle("story-title");
        syndEntry.setLink("http://www.robonews.io/index.html");
        syndEntry.setGuid("test-unique-guid");
        syndEntry.setPubDate(new Date());
        syndEntry.setDescription("story-description");

        Category oldCategory = new Category();
        oldCategory.setId(1);
        oldCategory.setLevel(10);
        oldCategory.setPriority(false);

        Category newCategory = new Category();
        newCategory.setId(2);
        newCategory.setLevel(5);
        newCategory.setPriority(true);

        Feed newFeed = new Feed();
        newFeed.setCategory(newCategory);
        newFeed.setVideo(true);

        Story story = new Story();
        story.setCategory(oldCategory);
        story.setVideo(false);

        when(storyDao.getByGuid(any(Channel.class), eq("test-unique-guid"))).thenReturn(story);

        Story storyResult = crawlerDao.createStory(syndEntry, newFeed, new Date());

        verify(storyDao).update(story);
        verify(storyDao, times(0)).create(any(Story.class));

        Assert.assertTrue(story.isVideo());
        Assert.assertEquals(newCategory.getId(), story.getCategory().getId());
    }

}
