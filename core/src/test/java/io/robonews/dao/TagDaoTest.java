/**
 * Robonews.io
 *
 * Copyright (c) 2013-2015 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.dao;

import io.robonews.domain.Tag;
import io.robonews.domain.TagAlternative;
import org.junit.Assert;
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
@ActiveProfiles({"create-db", "fill-masterdata"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class TagDaoTest {

    @Autowired
    private TagDao tagDao;

    @Test
    public void testFindByAlternative() throws Exception {
        Tag tag1 = new Tag();
        tag1.setName("test-tag-1");
        tag1.setType(Tag.Type.MISC);
        tag1.setFreebaseMid("test-freebase-1");

        TagAlternative tagAlternative1 = new TagAlternative();
        tagAlternative1.setName("test-tag-alt-1");
        tagAlternative1.setType(Tag.Type.MISC);
        tagAlternative1.setTag(tag1);

        TagAlternative tagAlternative2 = new TagAlternative();
        tagAlternative2.setName("test-tag-alt-2");
        tagAlternative2.setTag(tag1);

        tag1.getTagAlternatives().add(tagAlternative1);
        tag1.getTagAlternatives().add(tagAlternative2);

        tagDao.create(tag1);

        Tag tag2 = tagDao.findByAlternative("test-tag-alt-1", Tag.Type.MISC);

        Assert.assertEquals("test-tag-1", tag2.getName());
    }

    @Test
    public void testFreebaseMidCaseSensitive() {
        Tag tag1 = new Tag();
        tag1.setName("test-tag-1");
        tag1.setType(Tag.Type.MISC);
        tag1.setFreebaseMid("ABC");

        Tag tag2 = new Tag();
        tag2.setName("test-tag-1");
        tag2.setType(Tag.Type.MISC);
        tag2.setFreebaseMid("AbC");

        Assert.assertNotEquals(tag1.hashCode(), tag2.hashCode());

        tagDao.create(tag1);
        tagDao.create(tag2);

        Tag tag3 = tagDao.findByFreebaseMid("ABC");
        Tag tag4 = tagDao.findByFreebaseMid("AbC");

        Assert.assertEquals(tag1, tag3);
        Assert.assertEquals(tag2, tag4);

        Assert.assertNotNull(tag3.getId());
        Assert.assertNotNull(tag4.getId());

        Assert.assertNotEquals(tag3.getId(), tag4.getId());
    }

    @Test
    public void testDiffTypes() {
        Tag tag1 = tagDao.createTagWithAlternative("Gore", Tag.Type.PERSON, "XXX", false, "Gore", Tag.Type.PERSON, 100);
        Tag tag2 = tagDao.createTagWithAlternative("Gore", Tag.Type.LOCATION, "YYY", false, "Gore", Tag.Type.LOCATION, 100);

        Assert.assertEquals(tag1, tagDao.findByAlternative("Gore", Tag.Type.PERSON));
        Assert.assertEquals(tag2, tagDao.findByAlternative("Gore", Tag.Type.LOCATION));
    }


}
