package io.robonews.console.controller;

import io.robonews.dao.CountryDao;
import io.robonews.domain.Country;
import io.robonews.domain.State;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Controller
@RequestMapping("rest")
public class CountryController {

    @Autowired
    private CountryDao countryDao;

    @RequestMapping(value = "/country/getAll", method = RequestMethod.GET)
    public @ResponseBody List<Map<String, String>> getAllCountries() {
        List<Map<String, String>> result = new LinkedList<>();

        for(Country country : countryDao.getAllCountries()) {
            Map<String, String> r = new HashMap<>();
            r.put("code", country.getIsoCode2());
            r.put("name", country.getName());

            result.add(r);
        }

        return result;
    }

    @RequestMapping(value = "/country/getStates/{countryCode}", method = RequestMethod.GET)
    public @ResponseBody List<Map<String, String>> getStates(@PathVariable String countryCode) {
        List<Map<String, String>> result = new LinkedList<>();

        for(State state : countryDao.getAllStates(countryCode)) {
            Map<String, String> r = new HashMap<>();
            r.put("code", state.getIsoCode());
            r.put("name", state.getName());

            result.add(r);
        }

        return result;
    }
}
