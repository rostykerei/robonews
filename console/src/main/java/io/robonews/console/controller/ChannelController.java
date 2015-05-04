/**
 * Robonews.io
 *
 * Copyright (c) 2013-2015 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.console.controller;

import io.robonews.console.controller.error.FormBindException;
import io.robonews.console.controller.error.NotFoundException;
import io.robonews.console.dao.ChannelConsoleDao;
import io.robonews.console.datatable.Datatable;
import io.robonews.console.datatable.DatatableCriteria;
import io.robonews.console.datatable.DatatableParams;
import io.robonews.console.dto.channel.ChannelDatatableItem;
import io.robonews.console.dto.channel.ChannelForm;
import io.robonews.console.dto.response.DataResponse;
import io.robonews.dao.ChannelDao;
import io.robonews.domain.Channel;
import io.robonews.service.alexa.AlexaService;
import io.robonews.service.alexa.impl.AlexaServiceResult;
import io.robonews.service.facebook.FacebookService;
import io.robonews.service.facebook.model.FacebookProfile;
import io.robonews.service.google.plus.GooglePlusService;
import io.robonews.service.google.plus.model.GooglePlusProfile;
import io.robonews.service.twitter.TwitterService;
import io.robonews.service.twitter.model.TwitterProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("rest")
public class ChannelController {

    @Autowired
    private ChannelConsoleDao channelConsoleDao;

    @Autowired
    private ChannelDao channelDao;

    @Autowired
    private AlexaService alexaService;

    @Autowired
    private FacebookService facebookService;

    @Autowired
    private TwitterService twitterService;

    @Autowired
    private GooglePlusService googlePlusService;

    @RequestMapping(value = "/channel/list", method = RequestMethod.GET)
    public @ResponseBody Datatable<ChannelDatatableItem> getChannelsDatatable(
            @DatatableParams(sortFields = {"canonicalName", "title", "url", "feedsCount"}) DatatableCriteria criteria) {

        return channelConsoleDao.getDatatable(criteria);
    }

    @RequestMapping(value = "/channel/{channelId}", method = RequestMethod.GET)
    public @ResponseBody ChannelForm getChannel(@PathVariable int channelId) {
        ChannelForm form = channelConsoleDao.getChannelForm(channelId);

        if (form == null) {
            throw new NotFoundException();
        }

        return form;
    }

    @RequestMapping(value = "/channel/new/prefill", method = RequestMethod.POST)
    public @ResponseBody DataResponse<ChannelForm> createChannelInit(@RequestParam("canonicalName") String canonicalName) {

        DataResponse<ChannelForm> response = new DataResponse<>();

        AlexaServiceResult alexaServiceResult;

        try {
            alexaServiceResult = alexaService.query(canonicalName);
        }
        catch (Exception e) {
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
        form.setActive(true);
        form.setCountry(alexaServiceResult.getCountryCode());

        response.setData(form);

        return response;
    }

    @RequestMapping(value = "/channel/save", method = RequestMethod.POST)
    public @ResponseBody DataResponse<Integer> saveChannel(@Valid @RequestBody ChannelForm form,  BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new FormBindException(bindingResult);
        }

        DataResponse<Integer> result = new DataResponse<>();

        try {
            Channel channel = channelConsoleDao.saveChannel(form);
            result.setData(channel.getId());
        }
        catch (Exception e) {
            result.setException(e);
            result.setError(true);
        }

        return result;
    }

    @RequestMapping(value = "/channel/fb-typeahead", method = RequestMethod.GET)
    public @ResponseBody List<FacebookProfile> fbTypeahead(@RequestParam("query") String query) {
        return facebookService.searchProfiles(query, 5);
    }

    @RequestMapping(value = "/channel/twitter-typeahead", method = RequestMethod.GET)
    public @ResponseBody List<TwitterProfile> twitterTypeahead(@RequestParam("query") String query) {
        return twitterService.searchProfiles(query, 5);
    }

    @RequestMapping(value = "/channel/google-plus-typeahead", method = RequestMethod.GET)
    public @ResponseBody List<GooglePlusProfile> googlePlusTypeahead(@RequestParam("query") String query) {
        return googlePlusService.searchProfiles(query, 5);
    }

    @ResponseBody
    @RequestMapping("/channel/image/{channelId}")
    public ResponseEntity<byte[]> getImage(@PathVariable int channelId) {

        Channel channel = channelDao.getById(channelId);

        if (channel == null || channel.getImage() == null) {
            throw new NotFoundException();
        }

        return ResponseEntity.ok()
                .header("Content-type", "image/png")
                .header("Cache-Control", "no-cache, no-store, must-revalidate")
                .header("Pragma", "no-cache")
                .header("Expires", "0")
                .body(channel.getImage().getData());
    }

}
