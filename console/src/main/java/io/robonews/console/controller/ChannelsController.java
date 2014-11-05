/**
 * Robonews.io
 *
 * Copyright (c) 2013-2014 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.console.controller;

import io.robonews.console.controller.error.BadRequestException;
import io.robonews.console.dao.ChannelConsoleDao;
import io.robonews.console.datatable.Datatable;
import io.robonews.console.datatable.DatatableCriteria;
import io.robonews.console.datatable.DatatableParams;
import io.robonews.console.dto.channel.ChannelDatatableItem;
import io.robonews.console.dto.channel.ChannelForm;
import io.robonews.console.dto.DataResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("rest")
public class ChannelsController {

    @Autowired
    private ChannelConsoleDao channelConsoleDao;

    @RequestMapping(value = "/channel/list", method = RequestMethod.GET)
    public @ResponseBody Datatable<ChannelDatatableItem> getChannelsDatatable(
            @DatatableParams(sortFields = {"canonicalName", "title", "url"}) DatatableCriteria criteria) {

        if (criteria.getSortField().equals("url")) {
            throw new BadRequestException("test exc");
        }

        return channelConsoleDao.getDatatable(criteria);
    }

    @RequestMapping(value = "/channel/{channelId}", method = RequestMethod.GET)
    public @ResponseBody ChannelForm getChannel(@PathVariable int channelId) {

        if (channelId == 2) {
            throw new BadRequestException("test exc");
        }

        return channelConsoleDao.getChannelForm(channelId);
    }

    @RequestMapping(value = "/channel/new/prefill", method = RequestMethod.POST)
    public @ResponseBody DataResponse<ChannelForm> createChannelInit(@RequestParam("canonicalName") String canonicalName) {

        DataResponse<ChannelForm> response = new DataResponse<>();

        if ("x".equals(canonicalName)) {
            response.setError(true);
            return response;
        }

        ChannelForm form = new ChannelForm();
        form.setCanonicalName(canonicalName);

        response.setData(form);

        return response;
    }

}
