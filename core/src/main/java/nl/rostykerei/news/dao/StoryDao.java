/**
 * Robonews.io
 *
 * Copyright (c) 2013-2014 Rosty Kerei.
 * All rights reserved.
 */
package nl.rostykerei.news.dao;

import nl.rostykerei.news.domain.Channel;
import nl.rostykerei.news.domain.Story;
import nl.rostykerei.news.domain.StoryImage;
import nl.rostykerei.news.domain.StoryTag;

public interface StoryDao extends AbstractDao<Story, Long> {

    Story getByIdWithTags(Long id);

    Story getByGuid(Channel channel, String storyGuid);

    void saveStoryTag(StoryTag storyTag);

    void saveStoryImage(StoryImage storyImage);

}
