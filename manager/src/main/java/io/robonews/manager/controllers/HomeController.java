/**
 * Robonews.io
 *
 * Copyright (c) 2013-2014 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.manager.controllers;

import io.robonews.domain.Channel;
import java.util.List;
import java.util.Map;
import io.robonews.dao.ChannelDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class HomeController {

    @Autowired
    private ChannelDao channelDao;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String home(Map<String, Object> map) {
        List<Channel> l = channelDao.getAll();
        map.put("channels", l);
        return "home";
    }
}
