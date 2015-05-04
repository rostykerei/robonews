/**
 * Robonews.io
 *
 * Copyright (c) 2013-2015 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.dao.hibernate;

import io.robonews.dao.GeoCategoryDao;
import io.robonews.domain.GeoCategory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

@Repository
public class GeoCategoryDaoHibernate
        extends NestedSetDaoHibernate<GeoCategory> implements GeoCategoryDao {

    private SessionFactory sessionFactory;

    public GeoCategoryDaoHibernate() {
        super(GeoCategory.class);
    }

    protected Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public GeoCategory createRoot(String name) {
        GeoCategory root = new GeoCategory();
        root.setName(name);

        return createAsRoot(root);
    }
}
