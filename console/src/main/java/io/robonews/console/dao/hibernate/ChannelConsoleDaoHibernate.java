/**
 * Robonews.io
 *
 * Copyright (c) 2013-2014 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.console.dao.hibernate;

import io.robonews.console.controller.error.NotFoundException;
import io.robonews.console.dao.ChannelConsoleDao;
import io.robonews.console.dao.DaoException;
import io.robonews.console.datatable.Datatable;
import io.robonews.console.datatable.DatatableCriteria;
import io.robonews.console.dto.channel.ChannelDatatableItem;
import io.robonews.console.dto.channel.ChannelForm;
import io.robonews.dao.ChannelDao;
import io.robonews.domain.Channel;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.*;
import org.hibernate.sql.JoinType;
import org.hibernate.transform.Transformers;
import org.springframework.transaction.annotation.Transactional;

/**
 * Hibernate implementation of ChannelConsoleDao
 *
 * @author Rosty Kerei
 */
public class ChannelConsoleDaoHibernate extends AbstractConsoleDaoHibernate implements ChannelConsoleDao {

    private ChannelDao channelDao;

    /**
     * Constructor
     *
     * @param sessionFactory Hibernate session factory
     * @param channelDao Default Channel DAO
     */
    public ChannelConsoleDaoHibernate(SessionFactory sessionFactory, ChannelDao channelDao) {
        super(sessionFactory);
        this.channelDao = channelDao;
    }

    @Override
    @Transactional(readOnly = true)
    public ChannelForm getChannelForm(int id) {
        Channel channel = channelDao.getById(id);

        if (channel == null) {
            return null;
        }

        return ChannelForm.fromChannel(channel);
    }

    @Override
    @Transactional(readOnly = true)
    @SuppressWarnings("unchecked")
    public Datatable<ChannelDatatableItem> getDatatable(DatatableCriteria criteria) {
        Datatable<ChannelDatatableItem> datatable = new Datatable<>();
        datatable.setDraw(criteria.getDraw());

        long recordsTotal = channelDao.getCountAll();
        datatable.setRecordsTotal(recordsTotal);

        Criteria c = getSession().createCriteria(Channel.class, "channel")
        .createAlias("channel.feeds", "feed", JoinType.LEFT_OUTER_JOIN)
        .setProjection(
            Projections.projectionList()
            .add(Projections.property("channel.canonicalName").as("canonicalName"))
            .add(Projections.property("channel.title").as("title"))
            .add(Projections.property("channel.url").as("url"))
            .add(Projections.property("channel.scale").as("scale"))
            .add(Projections.property("channel.active").as("active"))
            .add(Projections.count("feed.id").as("feedsCount"))
            .add(Projections.groupProperty("channel.id").as("id"))
        )
        .setResultTransformer(Transformers.aliasToBean(ChannelDatatableItem.class))
        .setFirstResult(criteria.getStart())
        .setMaxResults(criteria.getLength());

        if (DatatableCriteria.SortDirection.DESC.equals(criteria.getSortDirection())) {
            c.addOrder(Order.desc(criteria.getSortField()));
        }
        else {
            c.addOrder(Order.asc(criteria.getSortField()));
        }

        String search = criteria.getSearch();

        if (search != null && !search.isEmpty()) {
            Criterion searchCriterion = Restrictions.disjunction()
                .add(Restrictions.like("canonicalName", search, MatchMode.ANYWHERE))
                .add(Restrictions.like("title", search, MatchMode.ANYWHERE))
                .add(Restrictions.like("url", search, MatchMode.ANYWHERE));

            c.add(searchCriterion);

            Criteria countCriteria = getSession().createCriteria(Channel.class);
            countCriteria.add(searchCriterion);
            countCriteria.setProjection(Projections.rowCount());

            datatable.setRecordsFiltered((long) countCriteria.uniqueResult());
        }
        else {
            datatable.setRecordsFiltered(recordsTotal);
        }

        datatable.setData(c.list());

        return datatable;
    }

    @Override
    @Transactional
    public Channel saveChannel(ChannelForm channelForm) {
        int id = channelForm.getId();

        if (id > 0) {
            Channel channel = channelDao.getById(id);

            if (channel == null) {
                throw new DaoException("Channel id:" + id +  " does not exist");
            }

            channelDao.update(channelForm.updateChannel(channel));
        }
        else {
            id = channelDao.create(channelForm.toChannel());
        }

        return channelDao.getById(id);
    }

}
