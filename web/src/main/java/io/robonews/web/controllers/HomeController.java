/**
 * Robonews.io
 *
 * Copyright (c) 2013-2014 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.web.controllers;

import java.util.List;
import java.util.Map;
import io.robonews.dao.ChannelDao;
import io.robonews.domain.Channel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

    @Autowired
    private ChannelDao channelDao;

    @RequestMapping(value = "/")
    public String home(Map<String, Object> map) {
        List<Channel> l = channelDao.getAll();
        map.put("channels", l);
        return "home";
    }
}
