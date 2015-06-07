/**
 * Robonews.io
 *
 * Copyright (c) 2013-2015 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.console.dao.hibernate;

import io.robonews.console.dao.DaoException;
import io.robonews.console.dao.FeedConsoleDao;
import io.robonews.console.datatable.Datatable;
import io.robonews.console.datatable.DatatableCriteria;
import io.robonews.console.dto.feed.FeedDatatableItem;
import io.robonews.console.dto.feed.FeedForm;
import io.robonews.dao.AreaDao;
import io.robonews.dao.ChannelDao;
import io.robonews.dao.FeedDao;
import io.robonews.dao.TopicDao;
import io.robonews.domain.*;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.*;
import org.hibernate.sql.JoinType;
import org.hibernate.transform.Transformers;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * Created by rosty on 01/06/15.
 */
public class FeedConsoleDaoHibernate extends AbstractConsoleDaoHibernate implements FeedConsoleDao {

    private FeedDao feedDao;

    private ChannelDao channelDao;

    private AreaDao areaDao;

    private TopicDao topicDao;

    /**
     * Constructor
     *
     * @param sessionFactory Hibernate session factory
     */
    public FeedConsoleDaoHibernate(SessionFactory sessionFactory,
                                   FeedDao feedDao, ChannelDao channelDao,
                                   AreaDao areaDao, TopicDao topicDao) {
        super(sessionFactory);

        this.feedDao = feedDao;
        this.channelDao = channelDao;
        this.areaDao = areaDao;
        this.topicDao = topicDao;
    }

    @Override
    @Transactional
    public Feed saveFeed(FeedForm feedForm) {
        int id = feedForm.getId();

        Channel channel = channelDao.getById(feedForm.getChannelId());

        if (channel == null) {
            throw new DaoException("Channel ID:" + feedForm.getChannelId() + " does not exist");
        }

        Area area = areaDao.getById(feedForm.getAreaId());

        if (area == null) {
            throw new DaoException("Area ID:" + feedForm.getAreaId() + " does not exist");
        }

        Topic topic = topicDao.getById(feedForm.getTopicId());

        if (topic == null) {
            throw new DaoException("Topic ID:" + feedForm.getAreaId() + " does not exist");
        }

        if (id > 0) {
            Feed feed = feedDao.getById(id);
            feed = feedForm.updateFeed(feed);

            feed.setChannel(channel);
            feed.setArea(area);
            feed.setTopic(topic);

            feedDao.update(feed);
        }
        else {
            Feed feed = feedForm.toFeed();
            feed.setChannel(channel);
            feed.setArea(area);
            feed.setTopic(topic);

            feed.setPlannedCheck(new Date());

            id = feedDao.create(feed);
        }

        return feedDao.getById(id);
    }

    @Override
    @Transactional(readOnly = true)
    @SuppressWarnings("unchecked")
    public Datatable<FeedDatatableItem> getDatatable(DatatableCriteria criteria) {
        Datatable<FeedDatatableItem> datatable = new Datatable<>();
        datatable.setDraw(criteria.getDraw());

        long recordsTotal = feedDao.getCountAll();
        datatable.setRecordsTotal(recordsTotal);


        Criteria c = getSession().createCriteria(Feed.class, "feed")
            .createAlias("feed.channel", "channel", JoinType.LEFT_OUTER_JOIN)
            .setProjection(
                Projections.projectionList()
                    .add(Projections.property("feed.id").as("id"))
                    .add(Projections.property("channel.id").as("channelId"))
                    .add(Projections.property("channel.canonicalName").as("channelCN"))
                    .add(Projections.property("feed.name").as("name"))
                    .add(Projections.property("feed.url").as("url"))
                    .add(Projections.property("feed.video").as("video"))
            )
            .setResultTransformer(Transformers.aliasToBean(FeedDatatableItem.class))
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
                    .add(Restrictions.like("channel.canonicalName", search, MatchMode.ANYWHERE))
                    .add(Restrictions.like("feed.name", search, MatchMode.ANYWHERE))
                    .add(Restrictions.like("feed.url", search, MatchMode.ANYWHERE));

            c.add(searchCriterion);

            Criteria countCriteria = getSession()
                    .createCriteria(Feed.class, "feed")
                    .createAlias("feed.channel", "channel", JoinType.LEFT_OUTER_JOIN);

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
}
