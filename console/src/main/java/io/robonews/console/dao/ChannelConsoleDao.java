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
import io.robonews.domain.Channel;

/**
 * Console-related {@link io.robonews.domain.Channel} data-access-object
 *
 * @author Rosty Kerei
 */
public interface ChannelConsoleDao {

    /**
     * Returns ChannelForm
     *
     * @param id Channel ID
     * @return ChannelForm
     */
    public ChannelForm getChannelForm(int id);

    /**
     * Returns filled {@link io.robonews.console.datatable.Datatable}
     * of {@link io.robonews.console.dto.channel.ChannelDatatableItem}
     *
     * @param criteria Datatable criteria
     * @return Datatable
     */
    public Datatable<ChannelDatatableItem> getDatatable(DatatableCriteria criteria);

    public Channel createChannel(ChannelForm channelForm);

}
