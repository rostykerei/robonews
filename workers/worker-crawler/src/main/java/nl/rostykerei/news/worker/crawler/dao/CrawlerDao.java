package nl.rostykerei.news.worker.crawler.dao;

import nl.rostykerei.news.domain.Feed;
import nl.rostykerei.news.domain.Story;
import nl.rostykerei.news.service.syndication.SyndicationEntry;

import java.util.Date;

/**
 * Created with IntelliJ IDEA on 9/3/13 at 10:48 AM
 *
 * @author Rosty Kerei
 */
public interface CrawlerDao {

    /**
     * Creates new Story or returns null if already exists
     *
     * @param syndEntry
     * @return
     */
    Story createStory(SyndicationEntry syndEntry, Feed feed, Date checkTime);

}
