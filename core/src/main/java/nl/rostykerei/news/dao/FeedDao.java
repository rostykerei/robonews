package nl.rostykerei.news.dao;

import java.util.List;
import nl.rostykerei.news.domain.Feed;

public interface FeedDao extends AbstractDao<Feed, Integer> {

    List<Feed> getAll();

    /**
     * Returns top planned feed and sets inProgressSince = now
     *
     * @return feed
     */
    Feed pollFeedToProcess();

    void releaseFeedInProcess(Feed feed);
}
