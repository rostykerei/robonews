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
import io.robonews.console.dto.DataResponse;
import io.robonews.console.dto.channel.ChannelDatatableItem;
import io.robonews.console.dto.channel.ChannelForm;
import io.robonews.service.alexa.AlexaService;
import io.robonews.service.alexa.exception.AlexaServiceException;
import io.robonews.service.alexa.impl.AlexaServiceResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("rest")
public class ChannelsController {

    @Autowired
    private ChannelConsoleDao channelConsoleDao;

    @Autowired
    private AlexaService alexaService;

    @RequestMapping(value = "/channel/list", method = RequestMethod.GET)
    public @ResponseBody Datatable<ChannelDatatableItem> getChannelsDatatable(
            @DatatableParams(sortFields = {"canonicalName", "title", "url", "feedsCount"}) DatatableCriteria criteria) {

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

        AlexaServiceResult alexaServiceResult;

        try {
            alexaServiceResult = alexaService.query(canonicalName);
        }
        catch (AlexaServiceException e) {
            // TODO log it
            response.setException(e);
            response.setError(true);
            return response;
        }

        ChannelForm form = new ChannelForm();
        form.setCanonicalName(alexaServiceResult.getSiteBase());
        form.setTitle(alexaServiceResult.getTitle());
        form.setUrl("http://www." + alexaServiceResult.getSiteBase() + "/");
        form.setDescription(alexaServiceResult.getDescription());
        form.setAlexaRank(alexaServiceResult.getRank());

        response.setData(form);

        return response;
    }

}
