package nl.rostykerei.news.dao.hibernate;

import nl.rostykerei.news.dao.FeedDao;
import nl.rostykerei.news.domain.Feed;
import org.hibernate.LockMode;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA on 7/25/13 at 6:19 PM
 *
 * @author Rosty Kerei
 */
public class FeedDaoHibernate extends AbstractDaoHibernate<Feed, Integer> implements FeedDao {

    public FeedDaoHibernate() {
        super(Feed.class);
    }

    @Override
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
    public Feed getFeedToProcess() {
        Feed feed = (Feed) getSession().
                createQuery("from Feed f" +
                        " left join fetch f.category" +
                        " left join fetch f.channel" +
                        " where f.inProgressSince is null" +
                        " order by f.plannedCheck asc").
                setMaxResults(1).
                setLockMode("f", LockMode.UPGRADE_NOWAIT).
                uniqueResult();

        if (feed == null) {
            return null;
        }

        feed.setInProgressSince(new Date());
        update(feed);
        return feed;
    }

    @Override
    public void releaseFeedInProcess(Feed feed) {
        feed.setInProgressSince(null);
        feed.setLastCheck(new Date());
        update(feed);
    }
}
