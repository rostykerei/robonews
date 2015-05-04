package io.robonews.console.controller;

import io.robonews.console.dto.geoCategory.GeoCategoryDto;
import io.robonews.console.dto.response.DataResponse;
import io.robonews.dao.GeoCategoryDao;
import io.robonews.domain.GeoCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("rest")
public class GeoCategoryController {

    @Autowired
    private GeoCategoryDao geoCategoryDao;

    @RequestMapping(value = "/geo_category/list", method = RequestMethod.GET)
    public @ResponseBody List<GeoCategoryDto> getList() {
        return geoCategoryDao
                .getAll()
                .stream()
                .map(GeoCategoryDto::fromGeoCategory)
                .collect(Collectors.toList());
    }

    @RequestMapping(value = "/geo_category/save", method = RequestMethod.POST)
    public @ResponseBody DataResponse<Integer> save(
            @RequestParam("parentId") int parentId, @RequestParam("name") String name) {

        DataResponse<Integer> response = new DataResponse<>();

        GeoCategory geoCategory = new GeoCategory();
        geoCategory.setName(name);

        try {
            int id = geoCategoryDao.create(geoCategory, parentId);
            response.setData(id);
        }
        catch (Exception e) {
            response.setException(e);
            response.setError(true);
        }

        return response;
    }
}
