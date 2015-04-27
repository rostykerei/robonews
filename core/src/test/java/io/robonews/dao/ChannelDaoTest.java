/**
 * Robonews.io
 *
 * Copyright (c) 2013-2015 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.dao;

import io.robonews.domain.Channel;
import io.robonews.domain.ChannelImage;
import io.robonews.domain.Image;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@TransactionConfiguration(defaultRollback = true)
@ContextConfiguration({ "classpath:testContext.xml" })
@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles({"create-db", "fill-masterdata"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class ChannelDaoTest {

    @Autowired
    private ChannelDao channelDao;

    @Autowired
    private CountryDao countryDao;

    @Test
    public void testCRUD() throws Exception {
        Channel channel = new Channel();
        channel.setTitle("test-1");
        channel.setUrl("test-url-1");
        channel.setCanonicalName("test-1.com");
        channel.setScale(Channel.Scale.GLOBAL);
        channel.setCountry(countryDao.getByIsoCode2("US"));

        int id = channelDao.create(channel);

        Channel channel2 = channelDao.getById(id);
        Assert.assertEquals("test-1", channel2.getTitle());

        channel2.setTitle("test-1-updated");
        channelDao.update(channel2);

        Channel channel3 = channelDao.getById(id);
        Assert.assertEquals("test-1-updated", channel3.getTitle());
        Assert.assertEquals("US", channel3.getCountry().getIsoCode2());

        channelDao.delete(channel3);

        Channel channel4 = channelDao.getById(id);
        Assert.assertNull(channel4);
    }

    @Test
    public void testGetAll() throws Exception {
        List<Channel> list = channelDao.getAll();
        Assert.assertEquals(0, list.size());

        Channel channel = new Channel();
        channel.setTitle("test-1");
        channel.setUrl("test-url-1");
        channel.setCanonicalName("test-1.com");
        channel.setScale(Channel.Scale.GLOBAL);

        channelDao.create(channel);

        list = channelDao.getAll();
        Assert.assertEquals(1, list.size());

        Channel channel2 = new Channel();
        channel2.setTitle("test-2");
        channel2.setUrl("test-url-2");
        channel2.setCanonicalName("test-2.com");
        channel2.setScale(Channel.Scale.GLOBAL);

        channelDao.create(channel2);

        list = channelDao.getAll();
        Assert.assertEquals(2, list.size());

        channelDao.delete(channel);

        list = channelDao.getAll();
        Assert.assertEquals(1, list.size());
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void testUniqueness() {
        Channel channel = new Channel();
        channel.setTitle("test-unique");
        channel.setUrl("test-url-1");
        channel.setCanonicalName("test-1.com");
        channel.setScale(Channel.Scale.GLOBAL);

        channelDao.create(channel);

        Channel channel2 = new Channel();
        channel2.setTitle("test-unique");
        channel2.setUrl("test-url-1");
        channel2.setCanonicalName("test-1.com");
        channel2.setScale(Channel.Scale.GLOBAL);

        channelDao.create(channel2);
    }

    @Test
    public void testWithPicture() {
        Channel channel = new Channel();
        channel.setTitle("test-1");
        channel.setUrl("test-url-1");
        channel.setCanonicalName("test-1.com");
        channel.setScale(Channel.Scale.GLOBAL);

        ChannelImage channelImage = new ChannelImage();
        channelImage.setChannel(channel);
        channelImage.setType(Image.Type.PNG);
        channelImage.setData("TEST-PIC".getBytes());

        channel.setImage(channelImage);

        int id = channelDao.create(channel);

        Channel channel2 = channelDao.getById(id);
        Assert.assertEquals("test-1", channel2.getTitle());
        Assert.assertEquals("TEST-PIC", new String(channel2.getImage().getData()));

        ChannelImage channelImage2 = new ChannelImage();
        channelImage2.setChannel(channel2);
        channelImage2.setType(Image.Type.PNG);
        channelImage2.setData("TEST-PIC-2".getBytes());

        channel2.setImage(channelImage2);

        channelDao.update(channel2);

        Channel channel3 = channelDao.getById(id);
        Assert.assertEquals("test-1", channel3.getTitle());
        Assert.assertEquals("TEST-PIC-2", new String(channel3.getImage().getData()));
    }
}
