package nl.rostykerei.news.dao;

import java.util.List;
import nl.rostykerei.news.domain.Channel;

public interface ChannelDao extends AbstractDao<Channel, Integer> {

    List<Channel> getAll();

}
