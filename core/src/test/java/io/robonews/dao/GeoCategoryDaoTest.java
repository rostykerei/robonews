/**
 * Robonews.io
 *
 * Copyright (c) 2013-2015 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.dao;

import io.robonews.domain.GeoCategory;
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

import java.util.Iterator;
import java.util.List;

@TransactionConfiguration(defaultRollback = true)
@ContextConfiguration({ "classpath:testContext.xml" })
@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles({"create-db"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class GeoCategoryDaoTest {

    private GeoCategory worldCat;
    private GeoCategory usaCat;
    private GeoCategory canadaCat;

    @Autowired
    private GeoCategoryDao geoCategoryDao;

    /**
     * Helper method that creates a basic tree that looks as follows:
     *
     *              World
     *            /       \
     *         USA       Canada
     *
     */
    private void createBasicTree() {
        this.worldCat = geoCategoryDao.createRoot("World");

        this.usaCat = new GeoCategory();
        this.usaCat.setName("USA");

        this.canadaCat = new GeoCategory();
        this.canadaCat.setName("Canada");

        geoCategoryDao.create(this.usaCat, this.worldCat);
        geoCategoryDao.create(this.canadaCat, this.worldCat.getId());
    }

    @Test
    public void testCreateRoot() throws Exception {
        GeoCategory rootNewsCategory = geoCategoryDao.createRoot("World");

        GeoCategory world = geoCategoryDao.getById(rootNewsCategory.getId());

        Assert.assertEquals(0, world.getLevel());
        Assert.assertEquals(1, world.getLeftIndex());
        Assert.assertEquals(2, world.getRightIndex());
    }

    @Test
    public void testFetchTree() {
        createBasicTree();

        List<GeoCategory> tree = geoCategoryDao.getAll();
        assert tree.size() == 3;
        Iterator<GeoCategory> iter = tree.iterator();
        for (int i = 0; i < 3; i++) {
            GeoCategory node = iter.next();
            if (i == 0) {
                assert 1 == node.getLeftIndex();
                assert 6 == node.getRightIndex();
                assert 0 == node.getLevel();
            } else if (i == 1) {
                assert 2 == node.getLeftIndex();
                assert 3 == node.getRightIndex();
                assert 1 == node.getLevel();
            } else {
                assert 4 == node.getLeftIndex();
                assert 5 == node.getRightIndex();
                assert 1 == node.getLevel();
            }
        }
    }

    @Test
    public void testBasicTreeNavigation() {
        this.createBasicTree();

        GeoCategory worldCat2 = geoCategoryDao.getById(worldCat.getId());

        assert 1 == worldCat2.getLeftIndex();
        assert 6 == worldCat2.getRightIndex();
        assert 0 == worldCat2.getLevel();
        assert null == geoCategoryDao.getParent(worldCat2);

        List<GeoCategory> children = geoCategoryDao.getChildren(worldCat2);

        assert 2 == children.size();
        Iterator<GeoCategory> childIter = children.iterator();
        GeoCategory child1 = childIter.next();
        GeoCategory child2 = childIter.next();
        assert 2 == child1.getLeftIndex();
        assert 3 == child1.getRightIndex();
        assert !geoCategoryDao.hasChildren(child1);
        assert !geoCategoryDao.hasChildren(child2);
        assert 0 == geoCategoryDao.getChildren(child1).size();
        assert 0 == geoCategoryDao.getChildren(child2).size();

        assert worldCat2 == geoCategoryDao.getParent(child1);
        assert worldCat2 == geoCategoryDao.getParent(child2);

        assert !geoCategoryDao.hasParent(worldCat2);
        assert geoCategoryDao.hasParent(child1);
        assert geoCategoryDao.hasParent(child2);
    }

    @Test
    public void testAddingNodesToTree() {
        this.createBasicTree();

        GeoCategory root = geoCategoryDao.getById(worldCat.getId());

        assert 1 == root.getLeftIndex();
        assert 6 == root.getRightIndex();
        assert 2 == geoCategoryDao.getChildren(root).size();

        assert 3 == geoCategoryDao.getAll().size();

        GeoCategory ukCat = new GeoCategory();
        ukCat.setName("UK");
        geoCategoryDao.create(ukCat, root);

        assert 6 == ukCat.getLeftIndex();
        assert 7 == ukCat.getRightIndex();
        assert 8 == root.getRightIndex();
        assert 3 == geoCategoryDao.getChildren(root).size();

        GeoCategory germanyCat = new GeoCategory();
        germanyCat.setName("Germany");
        GeoCategory javaNode = geoCategoryDao.getById(usaCat.getId());
        geoCategoryDao.create(germanyCat, javaNode);

        assert 3 == geoCategoryDao.getChildren(root).size();
        assert 3 == germanyCat.getLeftIndex();
        assert 4 == germanyCat.getRightIndex();
        assert 2 == germanyCat.getLevel();
        assert 2 == javaNode.getLeftIndex();
        assert 5 == javaNode.getRightIndex();

        Assert.assertEquals(10, geoCategoryDao.getById(worldCat.getId()).getRightIndex());

        assert 5 == geoCategoryDao.getAll().size();
    }

    @Test
    public void testDeleteNode() {
        this.createBasicTree();

        GeoCategory root = geoCategoryDao.getById(worldCat.getId());
        assert 2 == geoCategoryDao.getChildren(root).size();
        assert 1 == root.getLeftIndex();
        assert 6 == root.getRightIndex();

        GeoCategory caCat2 = geoCategoryDao.getById(canadaCat.getId());

        geoCategoryDao.delete(caCat2);

        root = geoCategoryDao.getById(worldCat.getId());
        assert 1 == geoCategoryDao.getChildren(root).size();
        assert 1 == root.getLeftIndex();
        assert 4 == root.getRightIndex();
    }
}
