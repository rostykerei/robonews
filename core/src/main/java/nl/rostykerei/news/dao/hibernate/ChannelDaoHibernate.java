/**
 * Robonews.io
 *
 * Copyright (c) 2013-2014 Rosty Kerei.
 * All rights reserved.
 */
package nl.rostykerei.news.dao.hibernate;

import java.util.List;
import nl.rostykerei.news.dao.ChannelDao;
import nl.rostykerei.news.domain.Channel;
import org.springframework.transaction.annotation.Transactional;

public class ChannelDaoHibernate extends AbstractDaoHibernate<Channel, Integer> implements ChannelDao {

    public ChannelDaoHibernate() {
        super(Channel.class);
    }

    @Override
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public List<Channel> getAll() {
        return getSession().
                createQuery("from Channel order by name asc").
                list();
    }

}
