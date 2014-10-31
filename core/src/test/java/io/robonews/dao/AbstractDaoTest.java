/**
 * Robonews.io
 *
 * Copyright (c) 2013-2014 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.dao;

import io.robonews.domain.Channel;
import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

@TransactionConfiguration(defaultRollback = true)
@ContextConfiguration({ "classpath:testContext.xml" })
@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles({"create-db"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class AbstractDaoTest {

    @Autowired
    private ChannelDao channelDao;

    @Before
    public void setUp() throws Exception {

        for (int i = 0; i < 15; i++) {
            Channel channel = new Channel();
            channel.setName("abstract-test-" + i);
            channel.setUrl("abstract-test-" + i + "-url");
            channelDao.create(channel);
        }
    }

    @Test
    public void testTableCount() throws Exception {
        Assert.assertEquals(15, channelDao.getTableCount("", new String[0]));
        Assert.assertEquals(15, channelDao.getTableCount(null, null));

        Assert.assertEquals(1, channelDao.getTableCount("abstract-test-12", new String[] {"name"}));
        Assert.assertEquals(1, channelDao.getTableCount("12", new String[] {"name"}));
        Assert.assertEquals(1, channelDao.getTableCount("abstract-test-12-url", new String[] {"name", "url"}));

        Assert.assertEquals(6, channelDao.getTableCount("test-1", new String[] {"name"}));
        Assert.assertEquals(2, channelDao.getTableCount("2-url", new String[] {"name", "url"}));
    }

    @Test
    public void testTable() throws Exception {
        List<Channel> searchResult1 = channelDao.getTable(0, 5, "name", true, null, null);

        Assert.assertEquals(5, searchResult1.size());
        Assert.assertEquals("abstract-test-0", searchResult1.get(0).getName());
        Assert.assertEquals("abstract-test-12", searchResult1.get(4).getName()); // 0, 1, 10, 11, 12

        List<Channel> searchResult2 = channelDao.getTable(5, 5, "url", false, null, null);

        Assert.assertEquals(5, searchResult2.size());
        Assert.assertEquals("abstract-test-4", searchResult2.get(0).getName());
        Assert.assertEquals("abstract-test-13", searchResult2.get(4).getName()); // 4, 3, 2, 14, 13

        List<Channel> searchResult3 = channelDao.getTable(0, 500, "name", true, "test-1", new String[] {"name"});
        Assert.assertEquals(6, searchResult3.size());
        Assert.assertEquals("abstract-test-1", searchResult3.get(0).getName());
        Assert.assertEquals("abstract-test-14", searchResult3.get(5).getName()); // 1, 10, 11, 12, 13, 14


        List<Channel> searchResult4 = channelDao.getTable(1, 500, "url", false, "2-url", new String[] {"name", "url"});
        Assert.assertEquals(1, searchResult4.size());
        Assert.assertEquals("abstract-test-12", searchResult4.get(0).getName());
    }
}
