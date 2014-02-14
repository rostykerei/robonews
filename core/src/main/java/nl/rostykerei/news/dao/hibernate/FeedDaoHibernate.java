/**
 * Robonews.io
 *
 * Copyright (c) 2013-2014 Rosty Kerei.
 * All rights reserved.
 */
package nl.rostykerei.news.dao.hibernate;

import java.util.Date;
import java.util.List;
import nl.rostykerei.news.dao.FeedDao;
import nl.rostykerei.news.domain.Feed;
import org.hibernate.LockMode;
import org.springframework.transaction.annotation.Transactional;

public class FeedDaoHibernate extends AbstractDaoHibernate<Feed, Integer> implements FeedDao {

    public FeedDaoHibernate() {
        super(Feed.class);
    }

    @Override
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public List<Feed> getAll() {
        return getSession().
                createQuery("from Feed f" +
                        " left join fetch f.category" +
                        " left join fetch f.channel" +
                        " order by f.name").
                list();
    }

    @Override
    @Transactional
    public Feed pollFeedToProcess() {
        Feed feed = (Feed) getSession().
                createQuery("from Feed f" +
                        " left join fetch f.category" +
                        " left join fetch f.channel" +
                        " where f.inProcessSince is null" +
                        " and f.plannedCheck <= :now" +
                        " order by f.plannedCheck asc").
                setTimestamp("now", new Date()).
                setMaxResults(1).
                setLockMode("f", LockMode.UPGRADE_NOWAIT).
                uniqueResult();

        if (feed == null) {
            return null;
        }

        feed.setInProcessSince(new Date());
        update(feed);
        return feed;
    }

    @Override
    @Transactional
    public void releaseFeedInProcess(Feed feed) {
        feed.setInProcessSince(null);
        feed.setLastCheck(new Date());
        update(feed);
    }
}
