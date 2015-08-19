/**
 * Robonews.io
 *
 * Copyright (c) 2013-2015 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.dao.hibernate;

import io.robonews.dao.StoryDao;
import io.robonews.domain.*;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

public class StoryDaoHibernate extends AbstractDaoHibernate<Story, Long> implements StoryDao {

    public StoryDaoHibernate() {
        super(Story.class);
    }

    @Override
    @Transactional(readOnly = true)
    public Story getByIdWithTags(Long id) {
        return (Story) getSession().
                createQuery("from Story s " +
                        "left join fetch s.channel " +
                        "left join fetch s.area " +
                        "left join fetch s.topic " +
                        "left join fetch s.originalFeed " +
                        "left join fetch s.tags " +
                        "where s.id = :id").
                setLong("id", id).
                uniqueResult();
    }

    @Override
    @Transactional(readOnly = true)
    public Story getByGuid(Channel channel, String storyGuid) {
        return (Story) getSession().
                createQuery("from Story s " +
                        "left join fetch s.channel " +
                        "left join fetch s.area " +
                        "left join fetch s.topic " +
                        "left join fetch s.originalFeed " +
                        "where s.channel.id = :channelId " +
                        "and s.guidHash = :guidHash").
                setInteger("channelId", channel.getId()).
                setBinary("guidHash", DigestUtils.sha1(storyGuid)).
                setMaxResults(1).
                uniqueResult();
    }

    @Override
    @Transactional
    public void saveStoryTag(StoryTag storyTag) {
        getSession().save(storyTag);
    }

    @Override
    @Transactional
    public void saveStoryImage(StoryImage storyImage) {
        getSession().save(storyImage);
    }

    @Override
    @Transactional(readOnly = true)
    @SuppressWarnings("unchecked")
    public List<Story> getFeedStories(int feedId, Date dateFrom, Date dateTo, int limit) {
        return getSession().createQuery("from Story s " +
                "where s.originalFeed.id = :feedId and (s.createdDate between :dateFrom and :dateTo) " +
                "order by s.id desc")
                .setInteger("feedId", feedId)
                .setTimestamp("dateFrom", dateFrom)
                .setTimestamp("dateTo", dateTo)
                .setMaxResults(limit)
                .list();
    }
}
