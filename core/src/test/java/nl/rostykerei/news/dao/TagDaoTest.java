package nl.rostykerei.news.dao;

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
@ContextConfiguration({ "classpath:coreContext.xml" })
@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles({"test", "fill-masterdata"})
public class TagDaoTest {

    @Autowired
    private TagDao tagDao;

    @Test
    public void testFindByFreebaseMid() throws Exception {
        Assert.assertEquals(0, tagDao.getCountAll());

        Tag tag1 = new Tag();
        tag1.setName("test-tag-1");
        tag1.setType(Tag.Type.MISC);
        tag1.setFreebaseMid("test-freebase-1");

        tagDao.create(tag1);

        Tag tag2 = new Tag();
        tag2.setName("test-tag-2");
        tag2.setType(Tag.Type.LOCATION);
        tag2.setFreebaseMid("test-freebase-2");

        tagDao.create(tag2);

        Tag tag3 = tagDao.findByFreebaseMid("test-freebase-1");
        Assert.assertEquals(tag1.getId(), tag3.getId());

        Assert.assertNull(tagDao.findByFreebaseMid("non-existing-tag"));
    }

    @Test
    public void testFindByAlternative() throws Exception {
        Assert.assertEquals(0, tagDao.getCountAll());

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
        tagDao.logAmbiguous("xxx");
        tagDao.logAmbiguous("xxx");
        tagDao.logAmbiguous("xxx");

        tagDao.logAmbiguous("yyy");
        tagDao.logAmbiguous("yyy");

        Assert.assertEquals(3, tagDao.findTagAmbiguous("xxx").getEffort());
        Assert.assertEquals(2, tagDao.findTagAmbiguous("yyy").getEffort());
        Assert.assertNull(tagDao.findTagAmbiguous("zzz"));

    }
}
