/**
 * Robonews.io
 *
 * Copyright (c) 2013-2015 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.dao.hibernate;

import io.robonews.dao.TopicDao;
import io.robonews.domain.Topic;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

@Repository
public class TopicDaoHibernate
        extends NestedNodeDaoHibernate<Topic> implements TopicDao {

    public TopicDaoHibernate() {
        super(Topic.class);
    }

    @Override
    public Topic createRoot(String name) {
        Topic root = new Topic();
        root.setName(name);

        return createAsRoot(root);
    }
}
