/**
 * Robonews.io
 *
 * Copyright (c) 2013-2014 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.dao.hibernate;

import io.robonews.domain.Category;
import java.util.List;
import io.robonews.dao.CategoryDao;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class CategoryDaoHibernate implements CategoryDao {

    private SessionFactory sessionFactory;

    protected Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    @Transactional(readOnly = true)
    public Category getById(int id) {
        return (Category) getSession().get(Category.class, id);
    }

    @Override
    @Transactional
    public Category createRoot(String name) {
        Category root = new Category();
        root.setName(name);
        root.setLevel(0);
        root.setLeftIndex(1);
        root.setRightIndex(2);

        getSession().save(root);

        return root;
    }

    @Override
    @Transactional
    public int create(Category category, Category parentCategory) {
        if (category == parentCategory) {
            throw new IllegalArgumentException("Cannot add category as a child of itself.");
        }

        int newLeft = parentCategory.getRightIndex();
        int newRight = parentCategory.getRightIndex() + 1;

        shiftRLValues(newLeft, 0, 2);

        parentCategory.setRightIndex(parentCategory.getRightIndex() + 2);

        category.setLevel(parentCategory.getLevel() + 1);
        category.setLeftIndex(newLeft);
        category.setRightIndex(newRight);

        return (Integer) getSession().save(category);
    }

    @Override
    @Transactional
    public int create(Category category, int parentCategoryId) {
        Category parentCategory = getById(parentCategoryId);
        if (parentCategory == null) {
            throw new IllegalArgumentException("Parent category cannot be null");
        }

        return create(category, parentCategory);
    }

    @Override
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public List<Category> getAll() {
        return getSession().
                createQuery("from Category order by leftIndex asc").
                list();
    }

    @Override
    @Transactional(readOnly = true)
    public Category getParent(Category category) {
        if (category.getLevel() == 0) {
            return null;
        }

        return (Category) getSession().createQuery("from Category " +
                "where leftIndex < :left " +
                "and rightIndex > :right " +
                "order by rightIndex asc").
                setInteger("left", category.getLeftIndex()).
                setInteger("right", category.getRightIndex()).
                setMaxResults(1).
                uniqueResult();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Category> getChildren(Category category) {
        return getChildren(category, 0);
    }

    @Override
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public List<Category> getChildren(Category category, int depth) {
        StringBuilder hql = new StringBuilder("from Category " +
                "where leftIndex > :left " +
                "and rightIndex < :right");

        if (depth > 0) {
            hql.append(" and level <= :level");
        }

        hql.append(" order by leftIndex");

        Query query = getSession().createQuery(hql.toString());

        query.setInteger("left", category.getLeftIndex());
        query.setInteger("right", category.getRightIndex());

        if (depth > 0) {
            query.setInteger("level", category.getLevel() + depth);
        }

        return query.list();
    }

    @Override
    public boolean hasChildren(Category category) {
        return (category.getRightIndex() - category.getLeftIndex()) > 1;
    }

    @Override
    public boolean hasParent(Category category) {
        return category.getLevel() > 0;
    }

    @Override
    @Transactional
    public void delete(Category category) {
        getSession().createQuery("delete from Category cat " +
                "where cat.leftIndex >= :left " +
                "and cat.rightIndex <= :right").
                setInteger("left", category.getLeftIndex()).
                setInteger("right", category.getRightIndex()).
                executeUpdate();

        // Close gap in tree
        int first = category.getRightIndex() + 1;
        int delta = category.getLeftIndex() - category.getRightIndex() - 1;
        shiftRLValues(first, 0, delta);
    }

    private void shiftRLValues(int first, int last, int delta) {
        // Shift left values
        StringBuilder sbLeft = new StringBuilder();
        sbLeft.append("update Category cat")
            .append(" set cat.leftIndex = cat.leftIndex + :delta")
            .append(" where cat.leftIndex >= :first");

        if (last > 0) {
            sbLeft.append(" and cat.leftIndex <= :last");
        }

        Query qLeft = getSession().createQuery(sbLeft.toString());
        qLeft.setInteger("delta", delta);
        qLeft.setInteger("first", first);

        if (last > 0) {
            qLeft.setInteger("last", last);
        }

        qLeft.executeUpdate();

        // Shift right values
        StringBuilder sbRight = new StringBuilder();
        sbRight.append("update  Category cat")
                .append(" set cat.rightIndex = cat.rightIndex + :delta")
                .append(" where cat.rightIndex >= :first");

        if (last > 0) {
            sbRight.append(" and cat.rightIndex <= :last");
        }

        Query qRight = getSession().createQuery(sbRight.toString());
        qRight.setInteger("delta", delta);
        qRight.setInteger("first", first);
        if (last > 0) {
            qRight.setInteger("last", last);
        }

        qRight.executeUpdate();

        getSession().flush();
        getSession().clear();
    }
}
