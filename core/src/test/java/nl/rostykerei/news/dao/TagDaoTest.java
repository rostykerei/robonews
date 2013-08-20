package nl.rostykerei.news.dao;

import nl.rostykerei.news.domain.Tag;
import nl.rostykerei.news.domain.TagAlternative;
import nl.rostykerei.news.domain.TagAmbiguous;
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
@ContextConfiguration({ "classpath:coreContext.xml" })
@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles({"test", "fill-masterdata"})
public class TagDaoTest {

    @Autowired
    private TagDao tagDao;

    @Test
    public void testFindOrCreateTagWithAlternative() throws Exception {
        Assert.assertEquals(0, tagDao.getCountAll());

        Tag tag1 = tagDao.findOrCreateTagWithAlternative("test-tag-1", "test-tag-alt-1", 100f, "test-freebase-1", Tag.Type.MISC);

        Assert.assertEquals(1, tagDao.getCountAll());

        Tag tag2 = tagDao.findOrCreateTagWithAlternative("test-tag-1", "test-tag-alt-2", 100f, "test-freebase-1", Tag.Type.MISC);

        Assert.assertEquals(1, tagDao.getCountAll());

        Tag tag3 = tagDao.findOrCreateTagWithAlternative("test-tag-2", "test-tag-alt-3", 100f, "test-freebase-2", Tag.Type.MISC);

        Assert.assertEquals(2, tagDao.getCountAll());

        Assert.assertEquals(tag1, tag2);
        Assert.assertEquals(tag1, tagDao.findByAlternative("test-tag-alt-1"));
        Assert.assertEquals(tag1, tagDao.findByAlternative("test-tag-alt-2"));
        Assert.assertEquals(tag3, tagDao.findByAlternative("test-tag-alt-3"));
    }

    @Test
    public void testFindByAlternative() throws Exception {
        Tag tag1 = new Tag();
        tag1.setName("test-tag-1");
        tag1.setType(Tag.Type.MISC);
        tag1.setFreebaseMid("test-freebase-1");

        TagAlternative tagAlternative1 = new TagAlternative();
        tagAlternative1.setName("test-tag-alt-1");
        tagAlternative1.setTag(tag1);

        TagAlternative tagAlternative2 = new TagAlternative();
        tagAlternative2.setName("test-tag-alt-2");
        tagAlternative2.setTag(tag1);

        tag1.getTagAlternatives().add(tagAlternative1);
        tag1.getTagAlternatives().add(tagAlternative2);

        tagDao.create(tag1);

        Tag tag2 = tagDao.findByAlternative("test-tag-alt-1");

        Assert.assertEquals("test-tag-1", tag2.getName());
    }

    @Test
    public void testTagAmbiguous() throws Exception {

        tagDao.createTagAmbiguous("xxx");

        TagAmbiguous tagAmbiguous = tagDao.findTagAmbiguous("xxx");
        tagAmbiguous.incrementEffort();

        tagDao.saveTagAmbiguous(tagAmbiguous);

        TagAmbiguous tagAmbiguous2 = tagDao.findTagAmbiguous("xxx");

        Assert.assertEquals(1, tagAmbiguous2.getEffort());
    }
}
