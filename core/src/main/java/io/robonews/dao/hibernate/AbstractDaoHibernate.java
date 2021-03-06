/**
 * Robonews.io
 *
 * Copyright (c) 2013-2015 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.dao.hibernate;

import io.robonews.dao.AbstractDao;
import java.io.Serializable;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.*;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
abstract class AbstractDaoHibernate <T, PK extends Serializable>
        implements AbstractDao<T, PK> {

    private SessionFactory sessionFactory;

    private Class<T> type;

    protected AbstractDaoHibernate(Class<T> type) {
        this.type = type;
    }

    protected Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Transactional(readOnly = true)
    @SuppressWarnings("unchecked")
    public T getById(PK id) {
        return (T) getSession().get(type, id);
    }

    @Transactional
    @SuppressWarnings("unchecked")
    public PK create(T o) {
        return (PK) getSession().save(o);
    }

    @Transactional
    public void update(T o) {
        getSession().update(o);
    }

    @Transactional
    public void createOrUpdate(T o) {
        getSession().saveOrUpdate(o);
    }

    @Transactional
    public void delete(T o) {
        getSession().delete(o);
    }


    @Transactional
    public long getCountAll() {
        return (Long) getSession().createCriteria(type).setProjection(Projections.rowCount()).uniqueResult();
    }

}
