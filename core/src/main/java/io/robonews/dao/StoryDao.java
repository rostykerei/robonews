/**
 * Robonews.io
 *
 * Copyright (c) 2013-2014 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.dao;

import io.robonews.domain.Story;
import io.robonews.domain.Channel;
import io.robonews.domain.StoryImage;
import io.robonews.domain.StoryTag;

public interface StoryDao extends AbstractDao<Story, Long> {

    Story getByIdWithTags(Long id);

    Story getByGuid(Channel channel, String storyGuid);

    void saveStoryTag(StoryTag storyTag);

    void saveStoryImage(StoryImage storyImage);

}
