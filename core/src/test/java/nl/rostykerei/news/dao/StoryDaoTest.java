package nl.rostykerei.news.dao;

import java.util.Date;
import nl.rostykerei.news.domain.Category;
import nl.rostykerei.news.domain.Channel;
import nl.rostykerei.news.domain.Feed;
import nl.rostykerei.news.domain.Story;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

@TransactionConfiguration(defaultRollback = false)
@ContextConfiguration({ "classpath:coreContext.xml" })
@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles({"test", "fill-masterdata"})
public class StoryDaoTest {

    @Autowired
    private StoryDao storyDao;

    @Autowired
    private FeedDao feedDao;

    @Autowired
    private ChannelDao channelDao;

    @Autowired
    private CategoryDao categoryDao;

    @Test
    public void testGetByGuid() throws Exception {
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

        feedDao.create(feed);

        Story story1 = new Story();
        story1.setOriginalFeed(feed);
        story1.setCategory(rootCategory);
        story1.setChannel(channel);
        story1.setDescription("test-story-descr-1");
        story1.setTitle("test-story-title-1");
        story1.setLink("test-story-link-1");
        story1.setGuid("test-story-guid-1");
        story1.setCreatedDate(new Date());
        story1.setPublicationDate(new Date());

        storyDao.create(story1);

        Story story2 = new Story();
        story2.setOriginalFeed(feed);
        story2.setCategory(rootCategory);
        story2.setChannel(channel);
        story2.setDescription("test-story-descr-2");
        story2.setTitle("test-story-title-2");
        story2.setLink("test-story-link-2");
        story2.setGuid("test-story-guid-2");
        story2.setCreatedDate(new Date());
        story2.setPublicationDate(new Date());

        storyDao.create(story2);

        Story story3 = storyDao.getByGuid(channel, "test-story-guid-1");

        Assert.assertNotEquals(story2.getId(), story3.getId());
        Assert.assertEquals(story1.getId(), story3.getId());
    }

}
