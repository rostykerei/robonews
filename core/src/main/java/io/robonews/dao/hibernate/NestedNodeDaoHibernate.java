/**
 * Robonews.io
 *
 * Copyright (c) 2013-2015 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.dao.hibernate;

import io.robonews.dao.NestedNodeDao;
import io.robonews.domain.NestedNode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by rosty on 04/05/15.
 */
abstract public class NestedNodeDaoHibernate<T extends NestedNode>
    extends AbstractDaoHibernate<T, Integer>
        implements NestedNodeDao<T> {

    private SessionFactory sessionFactory;

    private String typeName;

    public NestedNodeDaoHibernate(Class<T> type) {
        super(type);
        typeName = type.getName();
    }

    @Override
    @Transactional
    public T createAsRoot(T rootCategory) {
        rootCategory.setLevel(0);
        rootCategory.setLeftIndex(1);
        rootCategory.setRightIndex(2);

        getSession().save(rootCategory);

        return rootCategory;
    }

    @Override
    @Transactional
    public int create(T category, T parentCategory) {
        if (category == parentCategory) {
            throw new IllegalArgumentException("Cannot add a category as a child of itself.");
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
    public int create(T node, int parentNodeId) {
        T parentCategory = getById(parentNodeId);
        if (parentCategory == null) {
            throw new IllegalArgumentException("Parent category cannot be null");
        }

        return create(node, parentCategory);
    }

    @Override
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public List<T> getAll() {
        return getSession().
                createQuery("from :type: order by leftIndex asc"
                        .replace(":type:", typeName))
                .list();
    }

    @Override
    @Transactional(readOnly = true)
    public List<T> getChildren(T category) {
        return getChildren(category, 0);
    }

    @Override
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public List<T> getChildren(T category, int depth) {
        StringBuilder hql = new StringBuilder("from :type: " +
                "where leftIndex > :left " +
                "and rightIndex < :right");

        if (depth > 0) {
            hql.append(" and level <= :level");
        }

        hql.append(" order by leftIndex");

        Query query = getSession()
            .createQuery(
                hql
                .toString()
                .replace(":type:", typeName)
            );

        query.setInteger("left", category.getLeftIndex());
        query.setInteger("right", category.getRightIndex());

        if (depth > 0) {
            query.setInteger("level", category.getLevel() + depth);
        }

        return query.list();
    }

    @Override
    public boolean hasChildren(T category) {
        return (category.getRightIndex() - category.getLeftIndex()) > 1;
    }

    @Override
    public boolean hasParent(T category) {
        return category.getLevel() > 0;
    }

    @Override
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public T getParent(T category) {
        if (category.getLevel() == 0) {
            return null;
        }

        //noinspection JpaQlInspection
        return (T) getSession().createQuery("from " + typeName + " " +
                "where leftIndex < :left " +
                "and rightIndex > :right " +
                "order by rightIndex asc").
                setInteger("left", category.getLeftIndex()).
                setInteger("right", category.getRightIndex()).
                setMaxResults(1).
                uniqueResult();
    }

    @Override
    @Transactional
    public void delete(T category) {
        getSession().createQuery("delete from " +typeName + " cat " +
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

    protected void shiftRLValues(int first, int last, int delta) {
        // Shift left values
        StringBuilder sbLeft = new StringBuilder();
        sbLeft.append("update :type: cat")
                .append(" set cat.leftIndex = cat.leftIndex + :delta")
                .append(" where cat.leftIndex >= :first");

        if (last > 0) {
            sbLeft.append(" and cat.leftIndex <= :last");
        }

        Query qLeft = getSession()
            .createQuery(
                sbLeft
                .toString()
                .replace(":type:", typeName)
            );

        qLeft.setInteger("delta", delta);
        qLeft.setInteger("first", first);

        if (last > 0) {
            qLeft.setInteger("last", last);
        }

        qLeft.executeUpdate();

        // Shift right values
        StringBuilder sbRight = new StringBuilder();
        sbRight.append("update :type: cat")
                .append(" set cat.rightIndex = cat.rightIndex + :delta")
                .append(" where cat.rightIndex >= :first");

        if (last > 0) {
            sbRight.append(" and cat.rightIndex <= :last");
        }

        Query qRight = getSession()
            .createQuery(
                sbRight
                .toString()
                .replace(":type:", typeName)
            );

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
