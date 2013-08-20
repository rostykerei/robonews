package nl.rostykerei.news.dao;

import nl.rostykerei.news.domain.Tag;
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
    public void testFindOrCreateNamedEntity() throws Exception {
        Assert.assertEquals(0, tagDao.getCountAll());

        Tag tag1 = tagDao.findOrCreateNamedEntity("test-tag-1", Tag.Type.MISC);

        Assert.assertEquals(1, tagDao.getCountAll());

        Assert.assertNotNull(tag1.getId());
        Assert.assertEquals("test-tag-1", tag1.getName());
        Assert.assertEquals(Tag.Type.MISC, tag1.getType());

        Tag tag2 = tagDao.findOrCreateNamedEntity("Test-Tag-1", Tag.Type.MISC);

        Assert.assertEquals(tag1.getId(), tag2.getId());
    }
}
