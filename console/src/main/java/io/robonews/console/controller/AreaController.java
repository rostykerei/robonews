/**
 * Robonews.io
 *
 * Copyright (c) 2013-2015 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.console.controller;

import io.robonews.console.dto.AreaDto;
import io.robonews.console.dto.response.DataResponse;
import io.robonews.dao.AreaDao;
import io.robonews.domain.Area;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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
                .map(AreaDto::fromGeoCategory)
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
}
