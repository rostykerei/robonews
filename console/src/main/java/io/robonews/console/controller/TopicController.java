/**
 * Robonews.io
 *
 * Copyright (c) 2013-2015 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.console.controller;

import io.robonews.console.dto.TopicDto;
import io.robonews.console.dto.response.DataResponse;
import io.robonews.dao.TopicDao;
import io.robonews.domain.Topic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("rest/topic")
public class TopicController {

    @Autowired
    private TopicDao topicDao;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public @ResponseBody List<TopicDto> getList() {
        return topicDao
                .getAll()
                .stream()
                .map(TopicDto::fromTopic)
                .collect(Collectors.toList());
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public @ResponseBody
    DataResponse<Integer> save(
            @RequestParam("parentId") int parentId,
            @RequestParam("name") String name, @RequestParam("priority") boolean priority) {

        DataResponse<Integer> response = new DataResponse<>();

        Topic topic = new Topic();
        topic.setName(name);
        topic.setPriority(priority);

        try {
            int id = topicDao.create(topic, parentId);
            response.setData(id);
        }
        catch (Exception e) {
            response.setException(e);
            response.setError(true);
        }

        return response;
    }
}
