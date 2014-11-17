/**
 * Robonews.io
 *
 * Copyright (c) 2013-2014 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.console.dao;

import io.robonews.console.datatable.Datatable;
import io.robonews.console.datatable.DatatableCriteria;
import io.robonews.console.dto.channel.ChannelDatatableItem;
import io.robonews.console.dto.channel.ChannelForm;

public interface ChannelConsoleDao {

    public ChannelForm getChannelForm(int id);

    public Datatable<ChannelDatatableItem> getDatatable(DatatableCriteria criteria);

}
