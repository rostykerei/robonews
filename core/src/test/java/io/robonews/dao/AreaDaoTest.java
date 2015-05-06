/**
 * Robonews.io
 *
 * Copyright (c) 2013-2015 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.dao;

import io.robonews.domain.Area;
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
public class AreaDaoTest {

    private Area worldCat;
    private Area usaCat;
    private Area canadaCat;

    @Autowired
    private AreaDao areaDao;

    /**
     * Helper method that creates a basic tree that looks as follows:
     *
     *              World
     *            /       \
     *         USA       Canada
     *
     */
    private void createBasicTree() {
        this.worldCat = areaDao.createRoot("World");

        this.usaCat = new Area();
        this.usaCat.setName("USA");

        this.canadaCat = new Area();
        this.canadaCat.setName("Canada");

        areaDao.create(this.usaCat, this.worldCat);
        areaDao.create(this.canadaCat, this.worldCat.getId());
    }

    @Test
    public void testCreateRoot() throws Exception {
        Area rootArea = areaDao.createRoot("World");

        Area world = areaDao.getById(rootArea.getId());

        Assert.assertEquals(0, world.getLevel());
        Assert.assertEquals(1, world.getLeftIndex());
        Assert.assertEquals(2, world.getRightIndex());
    }

    @Test
    public void testFetchTree() {
        createBasicTree();

        List<Area> tree = areaDao.getAll();
        assert tree.size() == 3;
        Iterator<Area> iter = tree.iterator();
        for (int i = 0; i < 3; i++) {
            Area node = iter.next();
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

        Area worldCat2 = areaDao.getById(worldCat.getId());

        assert 1 == worldCat2.getLeftIndex();
        assert 6 == worldCat2.getRightIndex();
        assert 0 == worldCat2.getLevel();
        assert null == areaDao.getParent(worldCat2);

        List<Area> children = areaDao.getChildren(worldCat2);

        assert 2 == children.size();
        Iterator<Area> childIter = children.iterator();
        Area child1 = childIter.next();
        Area child2 = childIter.next();
        assert 2 == child1.getLeftIndex();
        assert 3 == child1.getRightIndex();
        assert !areaDao.hasChildren(child1);
        assert !areaDao.hasChildren(child2);
        assert 0 == areaDao.getChildren(child1).size();
        assert 0 == areaDao.getChildren(child2).size();

        assert worldCat2 == areaDao.getParent(child1);
        assert worldCat2 == areaDao.getParent(child2);

        assert !areaDao.hasParent(worldCat2);
        assert areaDao.hasParent(child1);
        assert areaDao.hasParent(child2);
    }

    @Test
    public void testAddingNodesToTree() {
        this.createBasicTree();

        Area root = areaDao.getById(worldCat.getId());

        assert 1 == root.getLeftIndex();
        assert 6 == root.getRightIndex();
        assert 2 == areaDao.getChildren(root).size();

        assert 3 == areaDao.getAll().size();

        Area ukCat = new Area();
        ukCat.setName("UK");
        areaDao.create(ukCat, root);

        assert 6 == ukCat.getLeftIndex();
        assert 7 == ukCat.getRightIndex();
        assert 8 == root.getRightIndex();
        assert 3 == areaDao.getChildren(root).size();

        Area germanyCat = new Area();
        germanyCat.setName("Germany");
        Area javaNode = areaDao.getById(usaCat.getId());
        areaDao.create(germanyCat, javaNode);

        assert 3 == areaDao.getChildren(root).size();
        assert 3 == germanyCat.getLeftIndex();
        assert 4 == germanyCat.getRightIndex();
        assert 2 == germanyCat.getLevel();
        assert 2 == javaNode.getLeftIndex();
        assert 5 == javaNode.getRightIndex();

        Assert.assertEquals(10, areaDao.getById(worldCat.getId()).getRightIndex());

        assert 5 == areaDao.getAll().size();
    }

    @Test
    public void testDeleteNode() {
        this.createBasicTree();

        Area root = areaDao.getById(worldCat.getId());
        assert 2 == areaDao.getChildren(root).size();
        assert 1 == root.getLeftIndex();
        assert 6 == root.getRightIndex();

        Area caCat2 = areaDao.getById(canadaCat.getId());

        areaDao.delete(caCat2);

        root = areaDao.getById(worldCat.getId());
        assert 1 == areaDao.getChildren(root).size();
        assert 1 == root.getLeftIndex();
        assert 4 == root.getRightIndex();
    }
}
