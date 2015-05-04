/**
 * Robonews.io
 *
 * Copyright (c) 2013-2015 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.dao.hibernate;

import io.robonews.dao.NewsCategoryDao;
import io.robonews.domain.NewsCategory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

@Repository
public class NewsCategoryDaoHibernate
        extends NestedSetDaoHibernate<NewsCategory> implements NewsCategoryDao {

    private SessionFactory sessionFactory;

    public NewsCategoryDaoHibernate() {
        super(NewsCategory.class);
    }

    protected Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public NewsCategory createRoot(String name) {
        NewsCategory root = new NewsCategory();
        root.setName(name);

        return createAsRoot(root);
    }
}
