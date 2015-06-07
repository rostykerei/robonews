/**
 * Robonews.io
 *
 * Copyright (c) 2013-2015 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.console.controller;

import io.robonews.console.controller.error.FormBindException;
import io.robonews.console.dao.ChannelConsoleDao;
import io.robonews.console.dao.FeedConsoleDao;
import io.robonews.console.datatable.Datatable;
import io.robonews.console.datatable.DatatableCriteria;
import io.robonews.console.datatable.DatatableParams;
import io.robonews.console.dto.feed.FeedAlreadyExistsException;
import io.robonews.console.dto.feed.FeedDatatableItem;
import io.robonews.console.dto.feed.FeedForm;
import io.robonews.console.dto.response.DataResponse;
import io.robonews.dao.FeedDao;
import io.robonews.domain.Channel;
import io.robonews.domain.Feed;
import io.robonews.service.http.HttpRequest;
import io.robonews.service.http.HttpResponse;
import io.robonews.service.http.HttpService;
import io.robonews.service.http.impl.HttpRequestImpl;
import io.robonews.service.syndication.SyndicationFeed;
import io.robonews.service.syndication.SyndicationService;
import io.robonews.service.syndication.SyndicationServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.net.URL;
import java.util.Date;

@Controller
@RequestMapping("rest/feed")
public class FeedController {

    @Autowired
    HttpService httpService;

    @Autowired
    SyndicationService syndicationService;

    @Autowired
    ChannelConsoleDao channelConsoleDao;

    @Autowired
    FeedConsoleDao feedConsoleDao;

    @Autowired
    FeedDao feedDao;

    private static final double MIN_VELOCITY = (double) 1 / 6;
    private static final double MAX_VELOCITY = 60;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public @ResponseBody
    Datatable<FeedDatatableItem> getChannelsDatatable(
            @DatatableParams(sortFields = {"channelCN", "name", "url"}) DatatableCriteria criteria) {

        return feedConsoleDao.getDatatable(criteria);
    }

    @RequestMapping(value = "/new/prefill", method = RequestMethod.POST)
    public @ResponseBody  DataResponse<FeedForm> createChannelInit(@RequestParam("url") String url) {

        DataResponse<FeedForm> response = new DataResponse<>();

        if (feedDao.getByUrl(url) != null) {
            response.setError(true);
            response.setException(new FeedAlreadyExistsException("Feed " + url + " already exists"));
            return response;
        }

        HttpRequest httpRequest = new HttpRequestImpl(url);
        HttpResponse httpResponse = null;
        SyndicationFeed syndicationFeed = null;
        URL feedUrl = null;

        try {
            feedUrl = new URL(url);

            httpResponse = httpService.execute(httpRequest);
            syndicationFeed = syndicationService.loadFeed(httpResponse.getStream());

            if (syndicationFeed == null) {
                throw new SyndicationServiceException("Feed could not be loaded", new Exception());
            }
        }
        catch (IOException e) {
            response.setException(e);
            response.setError(true);
            return response;
        }
        finally {
            if (httpResponse != null) {
                httpResponse.releaseConnection();
            }
        }

        FeedForm form = new FeedForm();
        form.setUrl(url);
        form.setName(syndicationFeed.getTitle());
        form.setLink(syndicationFeed.getLink());
        form.setDescription(syndicationFeed.getDescription());
        form.setAuthor(syndicationFeed.getAuthor());
        form.setCopyright(syndicationFeed.getCopyright());
        form.setImageUrl(syndicationFeed.getImageUrl());
        form.setVideo(false);

        double minVelocity = MIN_VELOCITY;

        Channel guessedChannel = channelConsoleDao.guessByHostname(feedUrl.getHost());

        if (guessedChannel != null) {
            form.setChannelId(guessedChannel.getId());
            form.setChannelCN(guessedChannel.getCanonicalName());

            if (guessedChannel.getScale() == Channel.Scale.GLOBAL) {
                minVelocity = 1;
            }
            else if (guessedChannel.getScale() == Channel.Scale.REGIONAL) {
                minVelocity = .5;
            }
        }

        double estimatedVelocity = syndicationFeed.estimateVelocity();

        if (estimatedVelocity < minVelocity) {
            form.setVelocity(minVelocity);
        }
        else if (estimatedVelocity > MAX_VELOCITY) {
            form.setVelocity(MAX_VELOCITY);
        }
        else {
            form.setVelocity(estimatedVelocity);
        }

        form.setMinVelocityThreshold(minVelocity);
        form.setMaxVelocityThreshold(MAX_VELOCITY);
        form.setPlannedCheck(new Date());

        response.setData(form);

        return response;
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public @ResponseBody DataResponse<Integer> saveChannel(@Valid @RequestBody FeedForm form,  BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new FormBindException(bindingResult);
        }

        DataResponse<Integer> result = new DataResponse<>();

        try {
            Feed feed = feedConsoleDao.saveFeed(form);
            result.setData(feed.getId());
        }
        catch (Exception e) {
            result.setException(e);
            result.setError(true);
        }

        return result;
    }
}
