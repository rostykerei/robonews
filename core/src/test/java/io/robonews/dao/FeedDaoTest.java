/**
 * Robonews.io
 *
 * Copyright (c) 2013-2014 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.dao;

import io.robonews.domain.Category;
import io.robonews.domain.Channel;
import io.robonews.domain.Feed;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@TransactionConfiguration(defaultRollback = true)
@ContextConfiguration({ "classpath:testContext.xml" })
@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles({"create-db"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class FeedDaoTest {

    @Autowired
    private FeedDao feedDao;

    @Autowired
    private ChannelDao channelDao;

    @Autowired
    private CategoryDao categoryDao;

    @Test
    public void testGetAll() throws Exception {
        List<Feed> list = feedDao.getAll();
        Assert.assertEquals(0, list.size());

        Channel channel = new Channel();
        channel.setName("test-channel-1");
        channel.setUrl("test-url-1");

        channelDao.create(channel);

        Category rootCategory = categoryDao.createRoot("test-category-1");

        Feed feed = new Feed();
        feed.setChannel(channel);
        feed.setCategory(rootCategory);
        feed.setUrl("test-url-1");
        feed.setName("test-feed-1");
        feed.setLink("test-link-1");

        feedDao.createOrUpdate(feed);

        list = feedDao.getAll();
        Assert.assertEquals(1, list.size());

        Feed feed2 = new Feed();
        feed2.setChannel(channel);
        feed2.setCategory(rootCategory);
        feed2.setUrl("test-url-2");
        feed2.setName("test-feed-2");
        feed2.setLink("test-link-2");

        feedDao.createOrUpdate(feed2);

        list = feedDao.getAll();
        Assert.assertEquals(2, list.size());

        feedDao.delete(feed);

        list = feedDao.getAll();
        Assert.assertEquals(1, list.size());
    }

    @Test
    public void testGetOne() {
        List<Feed> list = feedDao.getAll();
        Assert.assertEquals(0, list.size());

        Channel channel = new Channel();
        channel.setName("test-channel-1");
        channel.setUrl("test-url-1");

        channelDao.create(channel);

        Category rootCategory = categoryDao.createRoot("test-category-1");

        Feed feed = new Feed();
        feed.setChannel(channel);
        feed.setCategory(rootCategory);
        feed.setUrl("test-url-1");
        feed.setName("test-feed-1");
        feed.setLink("test-link-1");

        int feedId = feedDao.create(feed);
        Feed feed2 = feedDao.getById(feedId);
        Assert.assertEquals(rootCategory.getId(), feed2.getCategory().getId());
        Assert.assertEquals(channel.getId(), feed2.getChannel().getId());
    }

    @Test
    public void getFeedToProcess() {
        List<Feed> list = feedDao.getAll();
        Assert.assertEquals(0, list.size());

        Assert.assertNull(feedDao.pollFeedToProcess());

        Channel channel = new Channel();
        channel.setName("test-channel-1");
        channel.setUrl("test-url-1");

        channelDao.create(channel);

        Category rootCategory = categoryDao.createRoot("test-category-1");

        Feed feed1 = new Feed();
        feed1.setChannel(channel);
        feed1.setCategory(rootCategory);
        feed1.setUrl("test-url-1");
        feed1.setName("test-feed-1");
        feed1.setLink("test-link-1");
        feed1.setPlannedCheck(new Date( new Date().getTime() - 10000 ));

        int feedId1 = feedDao.create(feed1);

        Feed feed2 = new Feed();
        feed2.setChannel(channel);
        feed2.setCategory(rootCategory);
        feed2.setUrl("test-url-2");
        feed2.setName("test-feed-2");
        feed2.setLink("test-link-2");
        feed2.setPlannedCheck(new Date( new Date().getTime() - 3600000 ));

        int feedId2 = feedDao.create(feed2);

        feed2 = feedDao.pollFeedToProcess();
        Assert.assertEquals(feedId2, feed2.getId());

        feed1 = feedDao.pollFeedToProcess();
        Assert.assertEquals(feedId1, feed1.getId());

        Assert.assertNull(feedDao.pollFeedToProcess());

        feedDao.releaseFeedInProcess(feed2);

        feed2 = feedDao.pollFeedToProcess();
        Assert.assertEquals(feedId2, feed2.getId());

        feedDao.releaseFeedInProcess(feed1);
        feedDao.releaseFeedInProcess(feed2);

        feed1 = feedDao.getById(feedId1);
        feed2 = feedDao.getById(feedId1);

        Assert.assertNull(feed1.getInProcessSince());
        Assert.assertNull(feed2.getInProcessSince());
    }
}
