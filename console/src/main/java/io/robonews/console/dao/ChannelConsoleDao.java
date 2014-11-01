/**
 * Robonews.io
 *
 * Copyright (c) 2013-2014 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.console.dao;

import io.robonews.console.dto.ChannelDatatableItem;
import io.robonews.dao.AbstractDao;
import io.robonews.dao.ChannelDao;
import io.robonews.domain.Channel;
import java.io.Serializable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ChannelConsoleDao extends AbstractTableDao<Channel, ChannelDatatableItem> {

    private static final String[] SEARCH_FIELDS = new String[] { "canonicalName", "title", "url" };

    @Autowired
    private ChannelDao channelDao;

    @Override
    protected AbstractDao<Channel, ? extends Serializable> getDao() {
        return channelDao;
    }

    @Override
    protected String[] getSearchFields() {
        return SEARCH_FIELDS;
    }

    @Override
    protected ChannelDatatableItem mapFromOriginal(Channel obj) {
        return ChannelDatatableItem.fromChannel(obj);
    }
}
