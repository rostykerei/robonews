package nl.rostykerei.news.dao;

import java.util.Date;
import java.util.UUID;
import nl.rostykerei.news.domain.Category;
import nl.rostykerei.news.domain.Channel;
import nl.rostykerei.news.domain.Feed;
import nl.rostykerei.news.domain.Story;
import nl.rostykerei.news.domain.Tag;
import nl.rostykerei.news.domain.TagAlternative;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

@TransactionConfiguration(defaultRollback = true)
@ContextConfiguration({ "classpath:datasourceContext.xml", "classpath:coreContext.xml" })
@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles({"create-db", "fill-masterdata"})
public class StoryDaoTest {

    @Autowired
    private StoryDao storyDao;

    @Autowired
    private FeedDao feedDao;

    @Autowired
    private ChannelDao channelDao;

    @Autowired
    private CategoryDao categoryDao;

    @Autowired
    private TagDao tagDao;

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

        Assert.assertEquals(story1.getContentHash(), story3.getContentHash());
        Assert.assertEquals(story1.getUid(), story3.getUid());
    }

    @Test
    public void testStoryTags() throws Exception {
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

        Tag tag1 = new Tag();
        tag1.setName("test-tag-1");
        tag1.setType(Tag.Type.MISC);
        tag1.setFreebaseMid("test-freebase-1");

        TagAlternative tagAlternative1 = new TagAlternative();
        tagAlternative1.setName("test-tag-alt-1");
        tagAlternative1.setType(Tag.Type.MISC);

        tagAlternative1.setTag(tag1);
        tag1.getTagAlternatives().add(tagAlternative1);

        Tag tag2 = new Tag();
        tag2.setName("test-tag-2");
        tag2.setType(Tag.Type.ORGANIZATION);
        tag2.setFreebaseMid("test-freebase-2");

        story1.getTags().add(tag1);
        story1.getTags().add(tag2);

        storyDao.update(story1);

        Tag tag3 = tagDao.findByAlternative("test-tag-alt-1", Tag.Type.MISC);

        Assert.assertEquals("test-tag-1", tag3.getName());
    }


    @Test
    public void testUuid() throws Exception {
        UUID uuid = UUID.randomUUID();

        String uuidStr = uuid.toString();
        Long uuidLong = uuid.getLeastSignificantBits();

        System.out.println();
    }
   /* @Test
    public void testStoryTagEntity() throws Exception {
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

        Tag tag1 = new Tag();
        tag1.setName("test-tag-1");
        tag1.setType(Tag.Type.MISC);
        tag1.setFreebaseMid("test-freebase-1");

        tagDao.create(tag1);

        //story1.getTags().add(tag1);

        StoryTag storyTag = new StoryTag(story1, tag1);
        storyDao.saveStoryTag(storyTag);

        Story story2 = storyDao.getByGuid(channel, "test-story-guid-1");
        Assert.assertEquals(1, story2.getTags().size());


    }          */
}
