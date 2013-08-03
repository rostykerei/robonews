package nl.rostykerei.news.dao;

import nl.rostykerei.news.domain.Channel;

import java.util.List;

public interface ChannelDao extends AbstractDao<Channel, Integer> {

    List getAll();

}
