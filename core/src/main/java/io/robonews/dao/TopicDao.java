/**
 * Robonews.io
 *
 * Copyright (c) 2013-2015 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.dao;

import io.robonews.domain.Topic;

public interface TopicDao extends NestedNodeDao<Topic> {

    Topic createRoot(String name);

}
