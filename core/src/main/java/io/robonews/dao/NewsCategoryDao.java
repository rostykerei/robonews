/**
 * Robonews.io
 *
 * Copyright (c) 2013-2015 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.dao;

import io.robonews.domain.NewsCategory;

public interface NewsCategoryDao extends NestedSetDao<NewsCategory> {

    NewsCategory createRoot(String name);

}
