/**
 * Robonews.io
 *
 * Copyright (c) 2013-2015 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.dao;

import io.robonews.domain.Area;
import io.robonews.domain.Channel;
import io.robonews.domain.Feed;
import io.robonews.domain.Topic;
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
    private TopicDao topicDao;

    @Autowired
    private AreaDao areaDao;

    @Test
    public void testGetAll() throws Exception {
        List<Feed> list = feedDao.getAll();
        Assert.assertEquals(0, list.size());

        Channel channel = new Channel();
        channel.setTitle("test-channel-1");
        channel.setUrl("test-url-1");
        channel.setCanonicalName("test-1.com");
        channel.setScale(Channel.Scale.GLOBAL);

        channelDao.create(channel);

        Area rootArea = areaDao.createRoot("world");
        Topic rootTopic = topicDao.createRoot("test-category-1");

        Feed feed = new Feed();
        feed.setChannel(channel);
        feed.setArea(rootArea);
        feed.setTopic(rootTopic);
        feed.setUrl("test-url-1");
        feed.setName("test-feed-1");
        feed.setLink("test-link-1");
        feed.setLastCheck(new Date(0L));
        feed.setPlannedCheck(new Date());

        feedDao.createOrUpdate(feed);

        list = feedDao.getAll();
        Assert.assertEquals(1, list.size());

        Feed feed2 = new Feed();
        feed2.setChannel(channel);
        feed2.setArea(rootArea);
        feed2.setTopic(rootTopic);
        feed2.setUrl("test-url-2");
        feed2.setName("test-feed-2");
        feed2.setLink("test-link-2");
        feed2.setLastCheck(new Date(0L));
        feed2.setPlannedCheck(new Date());

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
        channel.setTitle("test-channel-1");
        channel.setUrl("test-url-1");
        channel.setCanonicalName("test-1.com");
        channel.setScale(Channel.Scale.GLOBAL);

        channelDao.create(channel);

        Topic rootTopic = topicDao.createRoot("test-category-1");
        Area rootArea = areaDao.createRoot("world");

        Feed feed = new Feed();
        feed.setChannel(channel);
        feed.setArea(rootArea);
        feed.setTopic(rootTopic);
        feed.setUrl("test-url-1");
        feed.setName("test-feed-1");
        feed.setLink("test-link-1");
        feed.setLastCheck(new Date(0L));
        feed.setPlannedCheck(new Date());

        int feedId = feedDao.create(feed);
        Feed feed2 = feedDao.getById(feedId);
        Assert.assertEquals(rootArea.getId(), feed2.getArea().getId());
        Assert.assertEquals(rootTopic.getId(), feed2.getTopic().getId());
        Assert.assertEquals(channel.getId(), feed2.getChannel().getId());
    }

    @Test
    public void testGetByUrl() {
        List<Feed> list = feedDao.getAll();
        Assert.assertEquals(0, list.size());

        Channel channel = new Channel();
        channel.setTitle("test-channel-1");
        channel.setUrl("test-url-1");
        channel.setCanonicalName("test-1.com");
        channel.setScale(Channel.Scale.GLOBAL);

        channelDao.create(channel);

        Topic rootTopic = topicDao.createRoot("test-category-1");
        Area rootArea = areaDao.createRoot("world");

        Feed feed = new Feed();
        feed.setChannel(channel);
        feed.setArea(rootArea);
        feed.setTopic(rootTopic);
        feed.setUrl("test-url-1");
        feed.setName("test-feed-1");
        feed.setLink("test-link-1");
        feed.setLastCheck(new Date(0L));
        feed.setPlannedCheck(new Date());

        feedDao.create(feed);

        Feed feed2 = feedDao.getByUrl("test-url-1");
        Assert.assertEquals(rootArea.getId(), feed2.getArea().getId());
        Assert.assertEquals(rootTopic.getId(), feed2.getTopic().getId());
        Assert.assertEquals(channel.getId(), feed2.getChannel().getId());

        Feed feed3 = feedDao.getByUrl("non-existent-url");
        Assert.assertNull(feed3);
    }

    @Test
    public void getFeedToProcess() {
        List<Feed> list = feedDao.getAll();
        Assert.assertEquals(0, list.size());

        Assert.assertNull(feedDao.pollFeedToProcess());

        Channel channel = new Channel();
        channel.setTitle("test-channel-1");
        channel.setUrl("test-url-1");
        channel.setCanonicalName("test-1.com");
        channel.setScale(Channel.Scale.GLOBAL);

        channelDao.create(channel);

        Area rootArea = areaDao.createRoot("world");
        Topic rootTopic = topicDao.createRoot("test-category-1");

        Feed feed1 = new Feed();
        feed1.setChannel(channel);
        feed1.setArea(rootArea);
        feed1.setTopic(rootTopic);
        feed1.setUrl("test-url-1");
        feed1.setName("test-feed-1");
        feed1.setLink("test-link-1");
        feed1.setPlannedCheck(new Date(new Date().getTime() - 10000));
        feed1.setLastCheck(new Date(0L));

        int feedId1 = feedDao.create(feed1);

        Feed feed2 = new Feed();
        feed2.setChannel(channel);
        feed2.setArea(rootArea);
        feed2.setTopic(rootTopic);
        feed2.setUrl("test-url-2");
        feed2.setName("test-feed-2");
        feed2.setLink("test-link-2");
        feed2.setPlannedCheck(new Date(new Date().getTime() - 3600000));
        feed2.setLastCheck(new Date(0L));

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
