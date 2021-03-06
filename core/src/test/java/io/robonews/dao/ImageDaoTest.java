/**
 * Robonews.io
 *
 * Copyright (c) 2013-2015 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.dao;

import io.robonews.domain.*;
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


@TransactionConfiguration(defaultRollback = true)
@ContextConfiguration({ "classpath:testContext.xml" })
@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles({"create-db", "fill-masterdata"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class ImageDaoTest {

    @Autowired
    private StoryDao storyDao;

    @Autowired
    private FeedDao feedDao;

    @Autowired
    private ChannelDao channelDao;

    @Autowired
    private AreaDao areaDao;

    @Autowired
    private TopicDao topicDao;

    @Autowired
    private ImageDao imageDao;

    @Test
    public void testGetByUrl() throws Exception {
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

        feedDao.create(feed);

        Story story1 = new Story();
        story1.setOriginalFeed(feed);
        story1.setArea(rootArea);
        story1.setTopic(rootTopic);
        story1.setChannel(channel);
        story1.setDescription("test-story-descr-1");
        story1.setTitle("test-story-title-1");
        story1.setLink("test-story-link-1");
        story1.setGuid("test-story-guid-1");
        story1.setCreatedDate(new Date());
        story1.setPublicationDate(new Date());
        story1.setAdjustedPublicationDate(new Date());

        storyDao.create(story1);

        Image image = new Image();
        image.setSourceChannel(channel);
        image.setSourceStory(story1);
        image.setUrl("http://some-url.com/test.jpg");
        image.setpHash(new byte[]{0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08});
        image.setCrcHash(123L);
        image.setType(Image.Type.JPEG);
        image.setSize(1000L);
        image.setWidth(640);
        image.setHeight(480);

        imageDao.create(image);

        Image image2 = imageDao.getByUrl("http://some-url.com/test.jpg");

        Assert.assertEquals(123L, image2.getCrcHash());
    }
}
