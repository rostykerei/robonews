/**
 * Robonews.io
 *
 * Copyright (c) 2013-2015 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.dao.hibernate;

import io.robonews.dao.FeedDao;
import io.robonews.domain.Feed;
import org.hibernate.LockMode;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
                    " left join fetch f.area" +
                    " left join fetch f.topic" +
                    " left join fetch f.channel" +
                    " order by f.name").
            list();
    }

    @Override
    @Transactional(readOnly = true)
    public Feed getByUrl(String url) {
        return (Feed) getSession()
                .createQuery("from Feed where url = :url")
                .setString("url", url)
                .setMaxResults(1)
                .uniqueResult();
    }

    @Override
    @Transactional
    public Feed pollFeedToProcess() {
        List<Feed> list = pollFeedsToProcess(1);

        if (list.size() == 0) {
            return null;
        }

        return list.get(0);
    }

    @Override
    @Transactional
    @SuppressWarnings("unchecked")
    public List<Feed> pollFeedsToProcess(int num) {
        Date now = new Date();

        List<Feed> feeds = getSession().
                createQuery("from Feed f" +
                        " left join fetch f.area" +
                        " left join fetch f.topic" +
                        " left join fetch f.channel" +
                        " where f.inProcessSince is null" +
                        " and f.plannedCheck <= :now" +
                        " order by f.plannedCheck asc").
                setTimestamp("now", now).
                setMaxResults(num).
                setLockMode("f", LockMode.UPGRADE_NOWAIT).
                list();

        if (feeds.isEmpty()) {
            return feeds;
        }

        List<Integer> ids = new ArrayList<>();
        for (Feed feed : feeds) {
            feed.setInProcessSince(now);
            ids.add(feed.getId());
        }

        getSession().
                createQuery("update Feed set inProcessSince = :now " +
                        "where id in (:ids)")
                .setTimestamp("now", now)
                .setParameterList("ids", ids)
                .executeUpdate();

        return feeds;
    }

    @Override
    @Transactional
    public void releaseFeedInProcess(Feed feed) {
        feed.setInProcessSince(null);
        feed.setLastCheck(new Date());
        update(feed);
    }

    @Override
    @Transactional
    public int resetIdleFeeds(long busyMilliseconds) {
        return getSession().
            createQuery("update Feed set inProcessSince = null " +
                "where inProcessSince <= :dt")
            .setTimestamp("dt", new Date( System.currentTimeMillis() - busyMilliseconds ))
            .executeUpdate();
    }
}
