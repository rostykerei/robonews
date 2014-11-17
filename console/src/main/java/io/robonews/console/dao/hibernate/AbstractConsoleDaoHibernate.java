/**
 * Robonews.io
 *
 * Copyright (c) 2013-2014 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.console.dao.hibernate;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public abstract class AbstractConsoleDaoHibernate {

    private SessionFactory sessionFactory;

    public AbstractConsoleDaoHibernate(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    protected Session getSession() {
        return sessionFactory.getCurrentSession();
    }

/*    protected Criteria getTableCriteria(Criteria criteria, String search, String[] searchFields) {
        if (search != null && searchFields != null && !search.isEmpty() && searchFields.length > 0) {
            Disjunction searchDisjunction = Restrictions.disjunction();

            for (String field : searchFields) {
                searchDisjunction.add(Restrictions.like(field, search, MatchMode.ANYWHERE));
            }

            criteria.add(searchDisjunction);
        }

        return criteria;
    }

    @Transactional
    protected long getTableCount(Criteria criteria, String search, String[] searchFields) {
        return (Long) getTableCriteria(criteria, search, searchFields).setProjection(Projections.rowCount()).uniqueResult();
    }

    @Transactional(readOnly = true)
    protected List getTable(Criteria criteria, int start, int length, String sortField, boolean isSortAscending, String search, String[] searchFields) {
        return getTableCriteria(criteria, search, searchFields)
                .addOrder(isSortAscending ? Order.asc(sortField) : Order.desc(sortField))
                .setFirstResult(start)
                .setMaxResults(length)
                .list();
    }*/
}
