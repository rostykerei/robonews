package nl.rostykerei.news.dao;

import java.util.Iterator;
import java.util.List;
import nl.rostykerei.news.domain.Category;
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
@ActiveProfiles("test")
public class CategoryDaoTest {

    private Category progCat;
    private Category javaCat;
    private Category netCat;

    @Autowired
    private CategoryDao categoryDao;

    /**
     * Helper method that creates a basic tree that looks as follows:
     *
     *           Programming
     *            /       \
     *         Java       .NET
     *
     */
    private void createBasicTree() {
        this.progCat = categoryDao.createRoot("Programming");

        this.javaCat = new Category();
        this.javaCat.setName("Java");

        this.netCat = new Category();
        this.netCat.setName(".NET");

        categoryDao.create(this.javaCat, this.progCat);
        categoryDao.create(this.netCat, this.progCat);
    }

    @Test
    public void testCreateRoot() throws Exception {
        Category rootCategory = categoryDao.createRoot("root");

        Category category = categoryDao.getById(rootCategory.getId());

        Assert.assertEquals(0, category.getLevel());
        Assert.assertEquals(1, category.getLeftIndex());
        Assert.assertEquals(2, category.getRightIndex());
    }

    @Test
    public void testFetchTree() {
        createBasicTree();

        List<Category> tree = categoryDao.getAll();
        assert tree.size() == 3;
        Iterator<Category> iter = tree.iterator();
        for (int i = 0; i < 3; i++) {
            Category node = iter.next();
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

        Category progCat2 = categoryDao.getById(progCat.getId());

        assert 1 == progCat2.getLeftIndex();
        assert 6 == progCat2.getRightIndex();
        assert 0 == progCat2.getLevel();
        assert null == categoryDao.getParent(progCat2);

        List<Category> children = categoryDao.getChildren(progCat2);

        assert 2 == children.size();
        Iterator<Category> childIter = children.iterator();
        Category child1 = childIter.next();
        Category child2 = childIter.next();
        assert 2 == child1.getLeftIndex();
        assert 3 == child1.getRightIndex();
        assert false == categoryDao.hasChildren(child1);
        assert false == categoryDao.hasChildren(child2);
        assert 0 == categoryDao.getChildren(child1).size();
        assert 0 == categoryDao.getChildren(child2).size();

        assert progCat2 == categoryDao.getParent(child1);
        assert progCat2 == categoryDao.getParent(child2);

        assert false == categoryDao.hasParent(progCat2);
        assert true == categoryDao.hasParent(child1);
        assert true == categoryDao.hasParent(child2);
    }

    @Test
    public void testAddingNodesToTree() {
        this.createBasicTree();

        Category root = categoryDao.getById(progCat.getId());

        // Assert basic tree state, a Programming category with 2 child categories.
        assert 1 == root.getLeftIndex();
        assert 6 == root.getRightIndex();
        assert 2 == categoryDao.getChildren(root).size();

        assert 3 == categoryDao.getAll().size();

        Category phpCat = new Category();
        phpCat.setName("PHP");
        categoryDao.create(phpCat, root);

        assert 6 == phpCat.getLeftIndex();
        assert 7 == phpCat.getRightIndex();
        assert 8 == root.getRightIndex();
        assert 3 == categoryDao.getChildren(root).size();

        // Add Java EE category under Java
        Category jeeCat = new Category();
        jeeCat.setName("Java EE");
        Category javaNode = categoryDao.getById(javaCat.getId());
        categoryDao.create(jeeCat, javaNode);

        assert 3 == categoryDao.getChildren(root).size();
        assert 3 == jeeCat.getLeftIndex();
        assert 4 == jeeCat.getRightIndex();
        assert 2 == jeeCat.getLevel();
        assert 2 == javaNode.getLeftIndex();
        assert 5 == javaNode.getRightIndex();

        Assert.assertEquals(10, categoryDao.getById(progCat.getId()).getRightIndex());

        assert 5 == categoryDao.getAll().size();
    }

    @Test
    public void testDeleteNode() {
        this.createBasicTree();

        Category root = categoryDao.getById(progCat.getId());
        assert 2 == categoryDao.getChildren(root).size();
        assert 1 == root.getLeftIndex();
        assert 6 == root.getRightIndex();

        Category netCat2 = categoryDao.getById(netCat.getId());

        categoryDao.delete(netCat2);

        root = categoryDao.getById(progCat.getId());
        assert 1 == categoryDao.getChildren(root).size();
        assert 1 == root.getLeftIndex();
        assert 4 == root.getRightIndex();
    }
}
