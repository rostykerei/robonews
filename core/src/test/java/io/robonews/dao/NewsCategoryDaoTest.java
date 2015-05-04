/**
 * Robonews.io
 *
 * Copyright (c) 2013-2015 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.dao;

import io.robonews.domain.NewsCategory;
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
public class NewsCategoryDaoTest {

    private NewsCategory progCat;
    private NewsCategory javaCat;
    private NewsCategory netCat;

    @Autowired
    private NewsCategoryDao newsCategoryDao;

    /**
     * Helper method that creates a basic tree that looks as follows:
     *
     *           Programming
     *            /       \
     *         Java       .NET
     *
     */
    private void createBasicTree() {
        this.progCat = newsCategoryDao.createRoot("Programming");

        this.javaCat = new NewsCategory();
        this.javaCat.setName("Java");

        this.netCat = new NewsCategory();
        this.netCat.setName(".NET");

        newsCategoryDao.create(this.javaCat, this.progCat);
        newsCategoryDao.create(this.netCat, this.progCat.getId());
    }

    @Test
    public void testCreateRoot() throws Exception {
        NewsCategory rootNewsCategory = newsCategoryDao.createRoot("root");

        NewsCategory newsCategory = newsCategoryDao.getById(rootNewsCategory.getId());

        Assert.assertEquals(0, newsCategory.getLevel());
        Assert.assertEquals(1, newsCategory.getLeftIndex());
        Assert.assertEquals(2, newsCategory.getRightIndex());
    }

    @Test
    public void testFetchTree() {
        createBasicTree();

        List<NewsCategory> tree = newsCategoryDao.getAll();
        assert tree.size() == 3;
        Iterator<NewsCategory> iter = tree.iterator();
        for (int i = 0; i < 3; i++) {
            NewsCategory node = iter.next();
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

        NewsCategory progCat2 = newsCategoryDao.getById(progCat.getId());

        assert 1 == progCat2.getLeftIndex();
        assert 6 == progCat2.getRightIndex();
        assert 0 == progCat2.getLevel();
        assert null == newsCategoryDao.getParent(progCat2);

        List<NewsCategory> children = newsCategoryDao.getChildren(progCat2);

        assert 2 == children.size();
        Iterator<NewsCategory> childIter = children.iterator();
        NewsCategory child1 = childIter.next();
        NewsCategory child2 = childIter.next();
        assert 2 == child1.getLeftIndex();
        assert 3 == child1.getRightIndex();
        assert !newsCategoryDao.hasChildren(child1);
        assert !newsCategoryDao.hasChildren(child2);
        assert 0 == newsCategoryDao.getChildren(child1).size();
        assert 0 == newsCategoryDao.getChildren(child2).size();

        assert progCat2 == newsCategoryDao.getParent(child1);
        assert progCat2 == newsCategoryDao.getParent(child2);

        assert !newsCategoryDao.hasParent(progCat2);
        assert newsCategoryDao.hasParent(child1);
        assert newsCategoryDao.hasParent(child2);
    }

    @Test
    public void testAddingNodesToTree() {
        this.createBasicTree();

        NewsCategory root = newsCategoryDao.getById(progCat.getId());

        // Assert basic tree state, a Programming category with 2 child categories.
        assert 1 == root.getLeftIndex();
        assert 6 == root.getRightIndex();
        assert 2 == newsCategoryDao.getChildren(root).size();

        assert 3 == newsCategoryDao.getAll().size();

        NewsCategory phpCat = new NewsCategory();
        phpCat.setName("PHP");
        newsCategoryDao.create(phpCat, root);

        assert 6 == phpCat.getLeftIndex();
        assert 7 == phpCat.getRightIndex();
        assert 8 == root.getRightIndex();
        assert 3 == newsCategoryDao.getChildren(root).size();

        // Add Java EE category under Java
        NewsCategory jeeCat = new NewsCategory();
        jeeCat.setName("Java EE");
        NewsCategory javaNode = newsCategoryDao.getById(javaCat.getId());
        newsCategoryDao.create(jeeCat, javaNode);

        assert 3 == newsCategoryDao.getChildren(root).size();
        assert 3 == jeeCat.getLeftIndex();
        assert 4 == jeeCat.getRightIndex();
        assert 2 == jeeCat.getLevel();
        assert 2 == javaNode.getLeftIndex();
        assert 5 == javaNode.getRightIndex();

        Assert.assertEquals(10, newsCategoryDao.getById(progCat.getId()).getRightIndex());

        assert 5 == newsCategoryDao.getAll().size();
    }

    @Test
    public void testDeleteNode() {
        this.createBasicTree();

        NewsCategory root = newsCategoryDao.getById(progCat.getId());
        assert 2 == newsCategoryDao.getChildren(root).size();
        assert 1 == root.getLeftIndex();
        assert 6 == root.getRightIndex();

        NewsCategory netCat2 = newsCategoryDao.getById(netCat.getId());

        newsCategoryDao.delete(netCat2);

        root = newsCategoryDao.getById(progCat.getId());
        assert 1 == newsCategoryDao.getChildren(root).size();
        assert 1 == root.getLeftIndex();
        assert 4 == root.getRightIndex();
    }
}
