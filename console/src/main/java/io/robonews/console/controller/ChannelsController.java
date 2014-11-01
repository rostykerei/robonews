/**
 * Robonews.io
 *
 * Copyright (c) 2013-2014 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.console.controller;

import io.robonews.console.dao.ChannelConsoleDao;
import io.robonews.console.datatable.Datatable;
import io.robonews.console.datatable.DatatableCriteria;
import io.robonews.console.datatable.DatatableParams;
import io.robonews.console.dto.ChannelDatatableItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("rest")
public class ChannelsController {

    @Autowired
    private ChannelConsoleDao channelConsoleDao;

    @RequestMapping(value = "/channel/list", method = RequestMethod.GET)
    public @ResponseBody Datatable<ChannelDatatableItem> getChannelsDatatable(
            @DatatableParams(sortFields = {"canonicalName", "title", "url"}) DatatableCriteria criteria) {

        return channelConsoleDao.getDatatable(criteria);
    }

}
