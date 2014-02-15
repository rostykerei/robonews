/**
 * Robonews.io
 *
 * Copyright (c) 2013-2014 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.manager.controllers;

import io.robonews.dao.ChannelDao;
import io.robonews.domain.Channel;
import io.robonews.manager.dto.ChannelDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.ConcurrencyFailureException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
public class ChannelController extends AbstractController {

    @Autowired
    private ChannelDao channelDao;

    @RequestMapping(value = "/channel/list", method = RequestMethod.GET)
    public String showList(Model model) {
        model.addAttribute("channels", channelDao.getAll());

        return "channel/list";
    }

    @RequestMapping(value = "/channel/create", method = RequestMethod.GET)
    public String showCreateForm(Model model) {
        model.addAttribute("channel", new ChannelDto());
        return "channel/create";
    }

    @RequestMapping(value = {"/channel/{channelId}/edit"}, method = RequestMethod.GET)
    public String showEditForm(@PathVariable int channelId, Model model, RedirectAttributes attributes) {
        Channel channel = channelDao.getById(channelId);

        if (channel == null) {
            addRedirectErrorMessage(attributes, "Selected channel does not exist");
            return "redirect:/channel/list";
        }

        model.addAttribute("channel", new ChannelDto(channel));

        return "channel/edit";
    }

    @RequestMapping(value = {"/channel/create", "/channel/{channelId}/edit"}, method = RequestMethod.POST)
    public String saveAction(@Valid @ModelAttribute("channel") ChannelDto channel, BindingResult result,
                             RedirectAttributes attributes, Model model) {
        if (result.hasErrors()) {
            addModelErrorMessage(model, "Channel has not been saved");
            return "channel/edit";
        }
        else {
            try {
                channelDao.createOrUpdate(channel.toChannel());
            }
            catch (ConcurrencyFailureException e) {
                addModelErrorMessage(model, e, "Channel has not been saved, concurency failure");
                return "channel/edit";
            }
            catch (DataIntegrityViolationException e) {
                addModelErrorMessage(model, e, "Channel has not been saved, data integrity violation");
                return "channel/edit";
            }
            catch (Exception e) {
                addModelErrorMessage(model, e, "Channel has not been saved");
                return "channel/edit";
            }

            addRedirectInfoMessage(attributes, "Channel has been successfully saved");
            return "redirect:/channel/list";
        }
    }

    @RequestMapping(value = {"/channel/{channelId}/delete"})
    public String deleteAction(@PathVariable int channelId, RedirectAttributes attributes) {
        Channel channel = channelDao.getById(channelId);

        if (channel == null) {
            addRedirectErrorMessage(attributes, "Selected channel does not exists");
            return "redirect:/channel/list";
        }

        try {
            channelDao.delete(channel);
        }
        catch (DataIntegrityViolationException e) {
            addRedirectErrorMessage(attributes, e, "Cannot delete selected channel, data integrity violation");
            return "redirect:/channel/list";
        }
        catch (Exception e) {
            addRedirectErrorMessage(attributes, e, "Cannot delete selected channel");
            return "redirect:/channel/list";
        }

        addRedirectInfoMessage(attributes, "Channel has been successfully deleted");
        return "redirect:/channel/list";
    }
}
