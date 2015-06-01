/**
 * Robonews.io
 *
 * Copyright (c) 2013-2015 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.console.dao.hibernate;

import io.robonews.console.dao.FeedConsoleDao;
import io.robonews.dao.FeedDao;
import io.robonews.domain.Channel;
import org.hibernate.SessionFactory;

/**
 * Created by rosty on 01/06/15.
 */
public class FeedConsoleDaoHibernate extends AbstractConsoleDaoHibernate implements FeedConsoleDao {

    private FeedDao feedDao;

    /**
     * Constructor
     *
     * @param sessionFactory Hibernate session factory
     */
    public FeedConsoleDaoHibernate(SessionFactory sessionFactory,
                                   FeedDao feedDao) {
        super(sessionFactory);

        this.feedDao = feedDao;
    }
}
