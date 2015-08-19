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
public class SectionDaoTest {

    @Autowired
    private FeedDao feedDao;

    @Autowired
    private ChannelDao channelDao;

    @Autowired
    private AreaDao areaDao;

    @Autowired
    private TopicDao topicDao;

    @Autowired
    private SectionDao sectionDao;

    @Test
    public void testSections() {
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

        Feed feed2 = new Feed();
        feed2.setChannel(channel);
        feed2.setArea(rootArea);
        feed2.setTopic(rootTopic);
        feed2.setUrl("test-url-2");
        feed2.setName("test-feed-2");
        feed2.setLink("test-link-2");
        feed2.setLastCheck(new Date(0L));
        feed2.setPlannedCheck(new Date());

        feedDao.create(feed2);

        Section section = new Section();
        section.setName("test-section");
        section.setActive(true);

        section.getLeadFeeds().add(feed);
        section.getLeadFeeds().add(feed2);

        sectionDao.create(section);

        Section section2 = sectionDao.getById(section.getId());

        Assert.assertEquals("test-section", section2.getName());
        Assert.assertTrue(section2.isActive());
        Assert.assertEquals(2, section2.getLeadFeeds().size());

        section2.setName("test-section-updated");
        section2.setActive(false);

        section2.getLeadFeeds().remove(
                section2.getLeadFeeds().iterator().next()
        );

        sectionDao.update(section2);

        Section section3 = sectionDao.getById(section.getId());

        Assert.assertEquals("test-section-updated", section3.getName());
        Assert.assertFalse(section3.isActive());
        Assert.assertEquals(1, section3.getLeadFeeds().size());
    }

}