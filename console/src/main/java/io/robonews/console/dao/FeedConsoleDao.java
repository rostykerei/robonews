/**
 * Robonews.io
 *
 * Copyright (c) 2013-2015 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.console.dao;

import io.robonews.console.datatable.Datatable;
import io.robonews.console.datatable.DatatableCriteria;
import io.robonews.console.dto.feed.FeedDatatableItem;
import io.robonews.console.dto.feed.FeedForm;
import io.robonews.domain.Channel;
import io.robonews.domain.Feed;

/**
 * Created by rosty on 01/06/15.
 */
public interface FeedConsoleDao {

    Feed saveFeed(FeedForm feedForm);

    Datatable<FeedDatatableItem> getDatatable(DatatableCriteria criteria);

}
