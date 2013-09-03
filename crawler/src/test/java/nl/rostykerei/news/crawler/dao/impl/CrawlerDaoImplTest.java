package nl.rostykerei.news.crawler.dao.impl;

import java.util.Date;
import nl.rostykerei.news.crawler.dao.CrawlerDao;
import nl.rostykerei.news.dao.CategoryDao;
import nl.rostykerei.news.dao.ChannelDao;
import nl.rostykerei.news.dao.FeedDao;
import nl.rostykerei.news.service.syndication.SyndicationEntry;
import nl.rostykerei.news.service.syndication.impl.SyndicationEntryImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

@TransactionConfiguration(defaultRollback = false)
@ContextConfiguration({ "classpath:crawlerContext.xml" })
//@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
//@ActiveProfiles({"test", "create-db", "fill-masterdata"})
@ActiveProfiles({"test"})
public class CrawlerDaoImplTest {

    @Autowired
    private FeedDao feedDao;

    @Autowired
    private ChannelDao channelDao;

    @Autowired
    private CategoryDao categoryDao;

    @Autowired
    private CrawlerDao crawlerDao;

    @Test
    @Transactional
    public void testCreateStory() throws Exception {
        /*Channel channel = new Channel();
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
         */
        SyndicationEntry syndEntry = new SyndicationEntryImpl();
        syndEntry.setDescription("test-story-descr");
        syndEntry.setAuthor("test-story-author");
        syndEntry.setGuid("test-story-guid");
        syndEntry.setLink("test-story-link");
        syndEntry.setPubDate(new Date());
        syndEntry.setTitle("test-story-title");

        crawlerDao.createStory(syndEntry, feedDao.getById(1), new Date());
    }
}
