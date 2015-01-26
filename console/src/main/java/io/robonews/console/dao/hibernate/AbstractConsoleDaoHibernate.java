/**
 * Robonews.io
 *
 * Copyright (c) 2013-2015 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.console.dao.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 * Abstract Hibernate functionality for all Hibernate DAOs
 *
 * @author Rosty Kerei
 */
public abstract class AbstractConsoleDaoHibernate {

    private SessionFactory sessionFactory;

    /**
     * Constructor
     *
     * @param sessionFactory Hibernate session factory
     */
    public AbstractConsoleDaoHibernate(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    /**
     * Hibernate session getter
     *
     * @return Hibernate session
     */
    protected Session getSession() {
        return sessionFactory.getCurrentSession();
    }

}
