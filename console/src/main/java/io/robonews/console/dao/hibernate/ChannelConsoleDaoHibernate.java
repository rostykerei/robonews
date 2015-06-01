/**
 * Robonews.io
 *
 * Copyright (c) 2013-2015 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.console.dao.hibernate;

import io.robonews.console.dao.ChannelConsoleDao;
import io.robonews.console.dao.DaoException;
import io.robonews.console.datatable.Datatable;
import io.robonews.console.datatable.DatatableCriteria;
import io.robonews.console.dto.channel.ChannelDatatableItem;
import io.robonews.console.dto.channel.ChannelForm;
import io.robonews.dao.ChannelDao;
import io.robonews.dao.CountryDao;
import io.robonews.domain.*;
import io.robonews.service.image.tools.ImageAvatarGenerator;
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

    private CountryDao countryDao;

    private ChannelDao channelDao;

    /**
     * Constructor
     *
     * @param sessionFactory Hibernate session factory
     */
    public ChannelConsoleDaoHibernate(SessionFactory sessionFactory,
                              ChannelDao channelDao,
                              CountryDao countryDao) {
        super(sessionFactory);
        this.channelDao = channelDao;
        this.countryDao = countryDao;
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

        Country country = channelForm.getCountry() != null ?
                countryDao.getByIsoCode2(channelForm.getCountry()) : null;

        State state = (country != null && channelForm.getState() != null) ?
                countryDao.getState(country.getIsoCode2(), channelForm.getState()) : null;

        if (id > 0) {
            Channel channel = channelDao.getById(id);

            if (channel == null) {
                throw new DaoException("Channel id:" + id +  " does not exist");
            }

            channel = channelForm.updateChannel(channel);
            channel.setCountry(country);
            channel.setState(state);

            channelDao.update(channel);
        }
        else {
            Channel channel = channelForm.toChannel();

            channel.setCountry(country);
            channel.setState(state);

            ChannelImage channelImage = new ChannelImage();
            channelImage.setChannel(channel);
            channelImage.setType(Image.Type.PNG);
            channelImage.setData(
                    ImageAvatarGenerator.generateAvatar(channel.getTitle())
            );
            channel.setImage(channelImage);

            id = channelDao.create(channel);
        }

        return channelDao.getById(id);
    }

    @Override
    public Channel guessByHostname(String hostname) {

        while (hostname.indexOf('.') > 0) {
            Channel channel = channelDao.getByCN(hostname);

            if (channel != null) {
                return channel;
            }

            hostname = hostname.substring(hostname.indexOf('.') + 1);
        }

        return null;
    }

}
