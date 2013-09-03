package nl.rostykerei.news.dao.hibernate;

import nl.rostykerei.news.dao.StoryDao;
import nl.rostykerei.news.domain.Channel;
import nl.rostykerei.news.domain.Story;
import nl.rostykerei.news.domain.StoryTag;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.transaction.annotation.Transactional;

public class StoryDaoHibernate extends AbstractDaoHibernate<Story, Long> implements StoryDao {

    public StoryDaoHibernate() {
        super(Story.class);
    }

    @Override
    @Transactional(readOnly = true)
    public Story getByGuid(Channel channel, String storyGuid) {

        String guidHash = DigestUtils.sha1Hex(storyGuid);

        return (Story) getSession().
                createQuery("from Story s " +
                        "left join fetch s.channel " +
                        "left join fetch s.category " +
                        "left join fetch s.originalFeed " +
                        "where s.channel.id = :channelId " +
                        "and s.guidHash = :guidHash").
                setInteger("channelId", channel.getId()).
                setString("guidHash", guidHash).
                setMaxResults(1).
                uniqueResult();
    }

    @Override
    @Transactional
    public void saveStoryTag(StoryTag storyTag) {

        getSession().save(storyTag);
        //getSession().flush();
        //getSession().clear();
    }
}
