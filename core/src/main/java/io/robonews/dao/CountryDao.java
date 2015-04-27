package io.robonews.dao;

import io.robonews.domain.Country;

import java.util.List;

/**
 * Created by rosty on 27/04/15.
 */
public interface CountryDao {

    Country getById(Integer id);

    Country getByIsoCode2(String isoCode2);

    Country getByIsoCode3(String isoCode3);

    List<Country> getAll();
}
