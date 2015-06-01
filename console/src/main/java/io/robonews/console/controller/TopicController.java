/**
 * Robonews.io
 *
 * Copyright (c) 2013-2015 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.console.controller;

import io.robonews.console.controller.error.NotFoundException;
import io.robonews.console.dto.TopicDto;
import io.robonews.console.dto.response.DataResponse;
import io.robonews.dao.TopicDao;
import io.robonews.domain.Topic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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
    public @ResponseBody DataResponse<Integer> save(
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

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/rename/{id}", method = RequestMethod.POST)
    public void rename(@PathVariable("id") int id, @RequestParam("newName") String newName) {
        Topic topic = topicDao.getById(id);

        if (topic == null) {
            throw new NotFoundException();
        }

        topic.setName(newName);
        topicDao.update(topic);
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") int id) {
        Topic topic = topicDao.getById(id);

        if (topic == null) {
            throw new NotFoundException();
        }

        topicDao.delete(topic);
    }
}
