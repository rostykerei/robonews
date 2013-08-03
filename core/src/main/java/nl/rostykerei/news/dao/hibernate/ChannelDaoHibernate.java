package nl.rostykerei.news.dao.hibernate;

import nl.rostykerei.news.dao.ChannelDao;
import nl.rostykerei.news.domain.Channel;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class ChannelDaoHibernate extends AbstractDaoHibernate<Channel, Integer> implements ChannelDao {

    public ChannelDaoHibernate() {
        super(Channel.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Channel> getAll() {
        return getSession().
                createQuery("from Channel order by name asc").
                list();
    }

}
