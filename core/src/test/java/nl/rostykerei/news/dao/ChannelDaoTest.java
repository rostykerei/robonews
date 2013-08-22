package nl.rostykerei.news.dao;

import java.util.List;
import nl.rostykerei.news.domain.Channel;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

@TransactionConfiguration(defaultRollback = true)
@ContextConfiguration({ "classpath:coreContext.xml" })
@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles({"test", "create-db"})
public class ChannelDaoTest {

    @Autowired
    private ChannelDao channelDao;

    @Test
    public void testCRUD() throws Exception{
        Channel channel = new Channel();
        channel.setName("test-1");
        channel.setUrl("test-url-1");

        int id = channelDao.create(channel);

        Channel channel2 = channelDao.getById(id);
        Assert.assertEquals("test-1", channel2.getName());

        channel2.setName("test-1-updated");
        channelDao.update(channel2);

        Channel channel3 = channelDao.getById(id);
        Assert.assertEquals("test-1-updated", channel3.getName());

        channelDao.delete(channel3);

        Channel channel4 = channelDao.getById(id);
        Assert.assertNull(channel4);
    }

    @Test
    public void testGetAll() throws Exception {
        List<Channel> list = channelDao.getAll();
        Assert.assertEquals(0, list.size());

        Channel channel = new Channel();
        channel.setName("test-1");
        channel.setUrl("test-url-1");

        channelDao.create(channel);

        list = channelDao.getAll();
        Assert.assertEquals(1, list.size());

        Channel channel2 = new Channel();
        channel2.setName("test-2");
        channel2.setUrl("test-url-2");

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
        channel.setName("test-unique");
        channel.setUrl("test-url-1");

        channelDao.create(channel);

        Channel channel2 = new Channel();
        channel2.setName("test-unique");
        channel2.setUrl("test-url-1");

        channelDao.create(channel2);
    }
}
