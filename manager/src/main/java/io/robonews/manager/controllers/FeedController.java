/**
 * Robonews.io
 *
 * Copyright (c) 2013-2015 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.manager.controllers;


import io.robonews.dao.ChannelDao;
import io.robonews.dao.FeedDao;
import io.robonews.dao.TopicDao;
import io.robonews.domain.Channel;
import io.robonews.domain.Feed;
import io.robonews.domain.Topic;
import io.robonews.manager.datatable.PagingCriteria;
import io.robonews.manager.datatable.TableParam;
import io.robonews.manager.dto.CategoryDto;
import io.robonews.manager.dto.FeedDto;
import io.robonews.manager.dto.FeedUrlDto;
import io.robonews.service.http.HttpRequest;
import io.robonews.service.http.HttpResponse;
import io.robonews.service.http.HttpService;
import io.robonews.service.http.impl.HttpRequestImpl;
import io.robonews.service.syndication.SyndicationFeed;
import io.robonews.service.syndication.SyndicationService;
import io.robonews.service.syndication.SyndicationServiceParsingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@SessionAttributes({"feedUrl", "feed"})
public class FeedController extends AbstractController {

    @Autowired
    private FeedDao feedDao;

    @Autowired
    private ChannelDao channelDao;

    @Autowired
    private TopicDao topicDao;

    @Autowired
    private HttpService httpService;

    @Autowired
    private SyndicationService syndicationService;

    @InitBinder
    public void setAllowedFields(WebDataBinder dataBinder) {
        dataBinder.setDisallowedFields("id");

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dataBinder.registerCustomEditor(Date.class, new CustomDateEditor(
                dateFormat, false));
    }

    @ModelAttribute("categories")
    public List<CategoryDto> parentCategoriesList(){
        List<CategoryDto> categories = new ArrayList<CategoryDto>();
        for (Topic topic : topicDao.getAll()) {
            CategoryDto parent = new CategoryDto();
            parent.setId(topic.getId());
            parent.setName(
                    new String(
                            new char[topic.getLevel()]).
                            replace("\0", "- ") +
                            topic.getName()
            );

            categories.add(parent);
        }

        return categories;
    }

    @ModelAttribute("channels")
    public List<Channel> channelsList() {
        return channelDao.getAll();
    }

    @RequestMapping(value="/feed/table", method=RequestMethod.GET, produces = "application/json" )
    public @ResponseBody List<Feed> getCustomers(@TableParam PagingCriteria criteria)
    {
        return feedDao.getAll();
    }

    @RequestMapping(value = {"/feed/list"}, method = RequestMethod.GET)
    public String showList(Model model) {
        model.addAttribute("feeds", feedDao.getAll());

        return "feed/list";
    }

    @RequestMapping(value = {"/feed/create"}, method = RequestMethod.GET)
    public String showCreateForm(Model model) {
        model.addAttribute("feedUrl", new FeedUrlDto());

        return "feed/create";
    }

    @RequestMapping(value = {"/feed/create"}, method = RequestMethod.POST)
    public String fillCreateForm(@Valid @ModelAttribute("feedUrl") FeedUrlDto feedUrl,
                                 BindingResult result, RedirectAttributes attributes, Model model,
                                 Errors errors) {

        if (result.hasErrors()) {
            addModelErrorMessage(model, "Failed to create new feed");
            return "feed/create";
        }

        HttpRequest httpRequest = new HttpRequestImpl(feedUrl.getUrl());
        HttpResponse httpResponse = null;
        SyndicationFeed syndicationFeed = null;

        try {
            httpResponse = httpService.execute(httpRequest);
            syndicationFeed = syndicationService.loadFeed(httpResponse.getStream());
        }
        catch (SyndicationServiceParsingException e) {
            errors.rejectValue("url", null, "incorrect URL");
            addModelErrorMessage(model, e, "Feed could not be parsed");
            return "feed/create";
        }
        catch (IOException e) {
            errors.rejectValue("url", null, "incorrect URL");
            addModelErrorMessage(model, e, "Cannot fetch URL");
            return "feed/create";
        }
        finally {
            if (httpResponse != null) {
                httpResponse.releaseConnection();
            }
        }

        if (syndicationFeed == null) {
            errors.rejectValue("url", null, "incorrect URL");
            addModelErrorMessage(model, "Feed could not be loaded");
            return "feed/create";
        }

        FeedDto feedDto = new FeedDto();
        feedDto.setUrl(feedUrl.getUrl());
        feedDto.setName(syndicationFeed.getTitle());
        feedDto.setLink(syndicationFeed.getLink());
        feedDto.setDescription(syndicationFeed.getDescription());
        feedDto.setAuthor(syndicationFeed.getAuthor());
        feedDto.setCopyright(syndicationFeed.getCopyright());
        feedDto.setImageUrl(syndicationFeed.getImageUrl());

        double estimatedVelocity = syndicationFeed.estimateVelocity();

        if (estimatedVelocity < 1/24) {
            feedDto.setVelocity(1/24);
        }
        else if (estimatedVelocity > 60) {
            feedDto.setVelocity(60);
        }
        else {
            feedDto.setVelocity(estimatedVelocity);
        }

        feedDto.setMinVelocityThreshold((double) 1 / 24);
        feedDto.setMaxVelocityThreshold(60);
        feedDto.setPlannedCheck(new Date());

        model.addAttribute("feed", feedDto);
        return "feed/edit";
    }

    @RequestMapping(value = {"/feed/{feedId}/edit"}, method = RequestMethod.GET)
    public String showEditForm(@PathVariable int feedId, Model model, RedirectAttributes attributes) {
        Feed feed = feedDao.getById(feedId);

        if (feed == null) {
            addRedirectErrorMessage(attributes, "Selected feed does not exist");
            return "redirect:/feed/list";
        }

        model.addAttribute("feed", new FeedDto(feed));

        return "feed/edit";
    }

    @RequestMapping(value = "/feed/save", method = RequestMethod.POST)
    public String saveAction(@Valid @ModelAttribute("feed") FeedDto feedDto, BindingResult result,
                             SessionStatus status, RedirectAttributes attributes, Model model) {
        if (result.hasErrors()) {
            addModelErrorMessage(model, "Feed has not been saved");
            return "feed/edit";
        }

        try {
            Feed feed = feedDto.toFeed();
            feed.setChannel(channelDao.getById(feedDto.getChannelId()));
            feed.setTopic(topicDao.getById(feedDto.getCategoryId()));

            feedDao.createOrUpdate(feed);
        }
        catch (DataIntegrityViolationException e) {
            addModelErrorMessage(model, e, "Feed has not been saved, data integrity violation");
            return "feed/edit";
        }
        catch (Exception e) {
            addModelErrorMessage(model, e, "Feed has not been saved");
            return "feed/edit";
        }

        status.setComplete();
        addRedirectInfoMessage(attributes, "Feed has been successfully saved");
        return "redirect:/feed/list";
    }

}
