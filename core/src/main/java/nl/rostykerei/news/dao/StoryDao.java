package nl.rostykerei.news.dao;

import nl.rostykerei.news.domain.Channel;
import nl.rostykerei.news.domain.Story;

public interface StoryDao  extends AbstractDao<Story, Long> {

    Story getByGuid(Channel channel, String storyGuid);

}
