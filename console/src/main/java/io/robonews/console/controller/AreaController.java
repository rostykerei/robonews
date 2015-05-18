/**
 * Robonews.io
 *
 * Copyright (c) 2013-2015 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.console.controller;

import io.robonews.console.controller.error.NotFoundException;
import io.robonews.console.dto.AreaDto;
import io.robonews.console.dto.response.DataResponse;
import io.robonews.dao.AreaDao;
import io.robonews.domain.Area;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("rest/area")
public class AreaController {

    @Autowired
    private AreaDao areaDao;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public @ResponseBody List<AreaDto> getList() {
        return areaDao
                .getAll()
                .stream()
                .map(AreaDto::fromArea)
                .collect(Collectors.toList());
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public @ResponseBody DataResponse<Integer> save(
            @RequestParam("parentId") int parentId, @RequestParam("name") String name) {

        DataResponse<Integer> response = new DataResponse<>();

        Area area = new Area();
        area.setName(name);

        try {
            int id = areaDao.create(area, parentId);
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
        Area area = areaDao.getById(id);

        if (area == null) {
            throw new NotFoundException();
        }

        area.setName(newName);
        areaDao.update(area);
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") int id) {
        Area area = areaDao.getById(id);

        if (area == null) {
            throw new NotFoundException();
        }

        areaDao.delete(area);
    }
}
