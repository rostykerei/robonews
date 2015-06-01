/**
 * Robonews.io
 *
 * Copyright (c) 2013-2015 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.console.controller;

import io.robonews.console.dao.ChannelConsoleDao;
import io.robonews.console.dto.feed.FeedForm;
import io.robonews.console.dto.response.DataResponse;
import io.robonews.domain.Channel;
import io.robonews.service.http.HttpRequest;
import io.robonews.service.http.HttpResponse;
import io.robonews.service.http.HttpService;
import io.robonews.service.http.impl.HttpRequestImpl;
import io.robonews.service.syndication.SyndicationFeed;
import io.robonews.service.syndication.SyndicationService;
import io.robonews.service.syndication.SyndicationServiceException;
import io.robonews.service.syndication.SyndicationServiceParsingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

@Controller
@RequestMapping("rest/feed")
public class FeedController {

    @Autowired
    HttpService httpService;

    @Autowired
    SyndicationService syndicationService;

    @Autowired
    ChannelConsoleDao channelConsoleDao;

    @RequestMapping(value = "/new/prefill", method = RequestMethod.POST)
    public @ResponseBody  DataResponse<FeedForm> createChannelInit(@RequestParam("url") String url) {

        DataResponse<FeedForm> response = new DataResponse<>();


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

        Channel guessedChannel = channelConsoleDao.guessByHostname(feedUrl.getHost());

        if (guessedChannel != null) {
            form.setChannelId(guessedChannel.getId());
        }

        response.setData(form);

        return response;
    }
}
