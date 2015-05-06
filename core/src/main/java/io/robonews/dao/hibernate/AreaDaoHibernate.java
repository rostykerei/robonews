/**
 * Robonews.io
 *
 * Copyright (c) 2013-2015 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.dao.hibernate;

import io.robonews.dao.AreaDao;
import io.robonews.domain.Area;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

@Repository
public class AreaDaoHibernate
        extends NestedNodeDaoHibernate<Area> implements AreaDao {

    private SessionFactory sessionFactory;

    public AreaDaoHibernate() {
        super(Area.class);
    }

    protected Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Area createRoot(String name) {
        Area root = new Area();
        root.setName(name);

        return createAsRoot(root);
    }
}
