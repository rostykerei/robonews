/**
 * Robonews.io
 *
 * Copyright (c) 2013-2015 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.dao;

import io.robonews.domain.*;

import java.util.Date;
import java.util.List;

public interface StoryDao extends AbstractDao<Story, Long> {

    Story getByIdWithTags(Long id);

    Story getByGuid(Channel channel, String storyGuid);

    void saveStoryTag(StoryTag storyTag);

    void saveStoryImage(StoryImage storyImage);

    List<Story> getFeedStories(int feedId, Date dateFrom, Date dateTo, int limit);
}
