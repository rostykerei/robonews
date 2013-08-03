package nl.rostykerei.news.dao;

import nl.rostykerei.news.domain.Category;
import nl.rostykerei.news.domain.Channel;
import nl.rostykerei.news.domain.Feed;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@TransactionConfiguration(defaultRollback = true)
@ContextConfiguration({ "classpath:coreContext.xml" })
@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
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

        Assert.assertNull(feedDao.getFeedToProcess());

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
        feed1.setPlannedCheck(new Date());

        int feedId1 = feedDao.create(feed1);

        Feed feed2 = new Feed();
        feed2.setChannel(channel);
        feed2.setCategory(rootCategory);
        feed2.setUrl("test-url-2");
        feed2.setName("test-feed-2");
        feed2.setLink("test-link-2");
        feed2.setPlannedCheck(new Date( new Date().getTime() - 1000 ));

        int feedId2 = feedDao.create(feed2);

        feed2 = feedDao.getFeedToProcess();
        Assert.assertEquals(feedId2, feed2.getId());

        feed1 = feedDao.getFeedToProcess();
        Assert.assertEquals(feedId1, feed1.getId());

        Assert.assertNull(feedDao.getFeedToProcess());

        feedDao.releaseFeedInProcess(feed2);

        feed2 = feedDao.getFeedToProcess();
        Assert.assertEquals(feedId2, feed2.getId());

        feedDao.releaseFeedInProcess(feed1);
        feedDao.releaseFeedInProcess(feed2);

        feed1 = feedDao.getById(feedId1);
        feed2 = feedDao.getById(feedId1);

        Assert.assertNull(feed1.getInProgressSince());
        Assert.assertNull(feed2.getInProgressSince());
    }
}
