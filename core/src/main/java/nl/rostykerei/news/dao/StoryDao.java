package nl.rostykerei.news.dao;

import nl.rostykerei.news.domain.Channel;
import nl.rostykerei.news.domain.Story;
import nl.rostykerei.news.domain.StoryTag;

public interface StoryDao  extends AbstractDao<Story, Long> {

    Story getByGuid(Channel channel, String storyGuid);

    void saveStoryTag(StoryTag storyTag);

}
