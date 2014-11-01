/**
 * Robonews.io
 *
 * Copyright (c) 2013-2014 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.dao.hibernate;

import io.robonews.dao.ChannelDao;
import io.robonews.domain.Channel;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class ChannelDaoHibernate extends AbstractDaoHibernate<Channel, Integer> implements ChannelDao {

    public ChannelDaoHibernate() {
        super(Channel.class);
    }

    @Override
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public List<Channel> getAll() {
        return getSession().
                createQuery("from Channel order by id asc").
                list();
    }

}
