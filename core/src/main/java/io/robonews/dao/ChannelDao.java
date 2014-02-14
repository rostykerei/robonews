/**
 * Robonews.io
 *
 * Copyright (c) 2013-2014 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.dao;

import io.robonews.domain.Channel;
import java.util.List;

public interface ChannelDao extends AbstractDao<Channel, Integer> {

    List<Channel> getAll();

}
