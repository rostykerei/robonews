/**
 * Robonews.io
 *
 * Copyright (c) 2013-2015 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.dao;

import io.robonews.domain.Topic;
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
public class TopicDaoTest {

    private Topic progCat;
    private Topic javaCat;
    private Topic netCat;

    @Autowired
    private TopicDao topicDao;

    /**
     * Helper method that creates a basic tree that looks as follows:
     *
     *           Programming
     *            /       \
     *         Java       .NET
     *
     */
    private void createBasicTree() {
        this.progCat = topicDao.createRoot("Programming");

        this.javaCat = new Topic();
        this.javaCat.setName("Java");

        this.netCat = new Topic();
        this.netCat.setName(".NET");

        topicDao.create(this.javaCat, this.progCat);
        topicDao.create(this.netCat, this.progCat.getId());
    }

    @Test
    public void testCreateRoot() throws Exception {
        Topic rootTopic = topicDao.createRoot("root");

        Topic topic = topicDao.getById(rootTopic.getId());

        Assert.assertEquals(0, topic.getLevel());
        Assert.assertEquals(1, topic.getLeftIndex());
        Assert.assertEquals(2, topic.getRightIndex());
    }

    @Test
    public void testFetchTree() {
        createBasicTree();

        List<Topic> tree = topicDao.getAll();
        assert tree.size() == 3;
        Iterator<Topic> iter = tree.iterator();
        for (int i = 0; i < 3; i++) {
            Topic node = iter.next();
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

        Topic progCat2 = topicDao.getById(progCat.getId());

        assert 1 == progCat2.getLeftIndex();
        assert 6 == progCat2.getRightIndex();
        assert 0 == progCat2.getLevel();
        assert null == topicDao.getParent(progCat2);

        List<Topic> children = topicDao.getChildren(progCat2);

        assert 2 == children.size();
        Iterator<Topic> childIter = children.iterator();
        Topic child1 = childIter.next();
        Topic child2 = childIter.next();
        assert 2 == child1.getLeftIndex();
        assert 3 == child1.getRightIndex();
        assert !topicDao.hasChildren(child1);
        assert !topicDao.hasChildren(child2);
        assert 0 == topicDao.getChildren(child1).size();
        assert 0 == topicDao.getChildren(child2).size();

        assert progCat2 == topicDao.getParent(child1);
        assert progCat2 == topicDao.getParent(child2);

        assert !topicDao.hasParent(progCat2);
        assert topicDao.hasParent(child1);
        assert topicDao.hasParent(child2);
    }

    @Test
    public void testAddingNodesToTree() {
        this.createBasicTree();

        Topic root = topicDao.getById(progCat.getId());

        // Assert basic tree state, a Programming category with 2 child categories.
        assert 1 == root.getLeftIndex();
        assert 6 == root.getRightIndex();
        assert 2 == topicDao.getChildren(root).size();

        assert 3 == topicDao.getAll().size();

        Topic phpCat = new Topic();
        phpCat.setName("PHP");
        topicDao.create(phpCat, root);

        assert 6 == phpCat.getLeftIndex();
        assert 7 == phpCat.getRightIndex();
        assert 8 == root.getRightIndex();
        assert 3 == topicDao.getChildren(root).size();

        // Add Java EE category under Java
        Topic jeeCat = new Topic();
        jeeCat.setName("Java EE");
        Topic javaNode = topicDao.getById(javaCat.getId());
        topicDao.create(jeeCat, javaNode);

        assert 3 == topicDao.getChildren(root).size();
        assert 3 == jeeCat.getLeftIndex();
        assert 4 == jeeCat.getRightIndex();
        assert 2 == jeeCat.getLevel();
        assert 2 == javaNode.getLeftIndex();
        assert 5 == javaNode.getRightIndex();

        Assert.assertEquals(10, topicDao.getById(progCat.getId()).getRightIndex());

        assert 5 == topicDao.getAll().size();
    }

    @Test
    public void testDeleteNode() {
        this.createBasicTree();

        Topic root = topicDao.getById(progCat.getId());
        assert 2 == topicDao.getChildren(root).size();
        assert 1 == root.getLeftIndex();
        assert 6 == root.getRightIndex();

        Topic netCat2 = topicDao.getById(netCat.getId());

        topicDao.delete(netCat2);

        root = topicDao.getById(progCat.getId());
        assert 1 == topicDao.getChildren(root).size();
        assert 1 == root.getLeftIndex();
        assert 4 == root.getRightIndex();
    }
}
