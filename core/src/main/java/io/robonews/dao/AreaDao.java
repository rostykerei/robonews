/**
 * Robonews.io
 *
 * Copyright (c) 2013-2015 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.dao;

import io.robonews.domain.Area;

/**
 * Created by rosty on 04/05/15.
 */
public interface AreaDao extends NestedSetDao<Area> {

    Area createRoot(String name);
}
