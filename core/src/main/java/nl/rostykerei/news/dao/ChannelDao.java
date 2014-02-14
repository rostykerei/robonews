/**
 * Robonews.io
 *
 * Copyright (c) 2013-2014 Rosty Kerei.
 * All rights reserved.
 */
package nl.rostykerei.news.dao;

import java.util.List;
import nl.rostykerei.news.domain.Channel;

public interface ChannelDao extends AbstractDao<Channel, Integer> {

    List<Channel> getAll();

}
