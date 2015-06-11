/**
 * Robonews.io
 *
 * Copyright (c) 2013-2015 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.worker.crawler.dao;

import io.robonews.dao.StoryDao;
import io.robonews.domain.*;
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

        Topic oldTopic = new Topic();
        oldTopic.setId(1);
        oldTopic.setLevel(10);
        oldTopic.setPriority(true);

        Topic newTopic = new Topic();
        newTopic.setId(2);
        newTopic.setLevel(20);
        newTopic.setPriority(true);

        Area area = new Area();
        area.setId(1);
        area.setLevel(1);

        Feed newFeed = new Feed();
        newFeed.setTopic(newTopic);
        newFeed.setVideo(true);
        newFeed.setArea(area);

        Story story = new Story();
        story.setTopic(oldTopic);
        story.setArea(area);
        story.setVideo(false);

        when(storyDao.getByGuid(any(Channel.class), eq("test-unique-guid"))).thenReturn(story);

        crawlerDao.createStory(syndEntry, newFeed, new Date());

        verify(storyDao).update(story);
        verify(storyDao, times(0)).create(any(Story.class));

        Assert.assertTrue(story.isVideo());
        Assert.assertEquals(newTopic.getId(), story.getTopic().getId());
    }

    @Test
    public void testCreateExistentSamePrioLevelStory() {
        SyndicationEntry syndEntry = new SyndicationEntryImpl();
        syndEntry.setTitle("story-title");
        syndEntry.setLink("http://www.robonews.io/index.html");
        syndEntry.setGuid("test-unique-guid");
        syndEntry.setPubDate(new Date());
        syndEntry.setDescription("story-description");

        Topic oldTopic = new Topic();
        oldTopic.setId(1);
        oldTopic.setLevel(10);
        oldTopic.setPriority(false);

        Topic newTopic = new Topic();
        newTopic.setId(2);
        newTopic.setLevel(5);
        newTopic.setPriority(true);

        Area area = new Area();
        area.setId(1);
        area.setLevel(1);

        Feed newFeed = new Feed();
        newFeed.setTopic(newTopic);
        newFeed.setVideo(true);
        newFeed.setArea(area);

        Story story = new Story();
        story.setTopic(oldTopic);
        story.setVideo(false);
        story.setArea(area);

        when(storyDao.getByGuid(any(Channel.class), eq("test-unique-guid"))).thenReturn(story);

        crawlerDao.createStory(syndEntry, newFeed, new Date());

        verify(storyDao).update(story);
        verify(storyDao, times(0)).create(any(Story.class));

        Assert.assertTrue(story.isVideo());
        Assert.assertEquals(newTopic.getId(), story.getTopic().getId());
    }

    @Test
    public void testCreateExistentSameArea() {
        SyndicationEntry syndEntry = new SyndicationEntryImpl();
        syndEntry.setTitle("story-title");
        syndEntry.setLink("http://www.robonews.io/index.html");
        syndEntry.setGuid("test-unique-guid");
        syndEntry.setPubDate(new Date());
        syndEntry.setDescription("story-description");

        Topic topic = new Topic();
        topic.setId(1);
        topic.setLevel(10);
        topic.setPriority(false);

        Area area = new Area();
        area.setId(1);
        area.setLevel(1);

        Feed newFeed = new Feed();
        newFeed.setTopic(topic);
        newFeed.setVideo(true);
        newFeed.setArea(area);

        Story story = new Story();
        story.setTopic(topic);
        story.setVideo(false);
        story.setArea(area);

        when(storyDao.getByGuid(any(Channel.class), eq("test-unique-guid"))).thenReturn(story);

        crawlerDao.createStory(syndEntry, newFeed, new Date());

        verify(storyDao).update(story);
        verify(storyDao, times(0)).create(any(Story.class));

        Assert.assertEquals(area.getId(), story.getArea().getId());
    }

    @Test
    public void testCreateExistentDetailedArea() {
        SyndicationEntry syndEntry = new SyndicationEntryImpl();
        syndEntry.setTitle("story-title");
        syndEntry.setLink("http://www.robonews.io/index.html");
        syndEntry.setGuid("test-unique-guid");
        syndEntry.setPubDate(new Date());
        syndEntry.setDescription("story-description");

        Topic topic = new Topic();
        topic.setId(1);
        topic.setLevel(10);
        topic.setPriority(false);

        Area area1 = new Area();
        area1.setId(1);
        area1.setLevel(1);

        Area area2 = new Area();
        area2.setId(2);
        area2.setLevel(2);

        Feed newFeed = new Feed();
        newFeed.setTopic(topic);
        newFeed.setVideo(true);
        newFeed.setArea(area2);

        Story story = new Story();
        story.setTopic(topic);
        story.setVideo(false);
        story.setArea(area1);

        when(storyDao.getByGuid(any(Channel.class), eq("test-unique-guid"))).thenReturn(story);

        crawlerDao.createStory(syndEntry, newFeed, new Date());

        verify(storyDao).update(story);
        verify(storyDao, times(0)).create(any(Story.class));

        Assert.assertTrue(story.isVideo());
        Assert.assertEquals(area2.getId(), story.getArea().getId());
    }
}
