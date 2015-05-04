package io.robonews.dao;

import io.robonews.domain.GeoCategory;

/**
 * Created by rosty on 04/05/15.
 */
public interface GeoCategoryDao extends NestedSetDao<GeoCategory> {

    GeoCategory createRoot(String name);
}
