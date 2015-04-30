/**
 * Robonews.io
 *
 * Copyright (c) 2013-2015 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.dao;

import io.robonews.domain.Country;
import io.robonews.domain.State;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@TransactionConfiguration(defaultRollback = true)
@ContextConfiguration({ "classpath:testContext.xml" })
@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles({"create-db", "fill-masterdata"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class CountryDaoTest {

    @Autowired
    private CountryDao countryDao;

    @Test
    public void testGetById() throws Exception {
        Country us = countryDao.getById(1);

        Assert.assertEquals(1, us.getId());
        Assert.assertEquals("US", us.getIsoCode2());
        Assert.assertEquals("USA", us.getIsoCode3());

        Assert.assertTrue(us.getStates().size() == 51);
    }

    @Test
    public void testGetByIsoCode2() throws Exception {
        Country nl = countryDao.getByIsoCode2("NL");

        Assert.assertEquals("NL", nl.getIsoCode2());
        Assert.assertEquals("NLD", nl.getIsoCode3());
    }

    @Test
    public void testGetByIsoCode3() throws Exception {
        Country ca = countryDao.getByIsoCode3("CAN");

        Assert.assertEquals("CA", ca.getIsoCode2());
        Assert.assertEquals("CAN", ca.getIsoCode3());
    }

    @Test
    public void getAllCountries() throws Exception {
        List<Country> list = countryDao.getAllCountries();

        Assert.assertTrue(list.size() > 200);
    }

    @Test
    public void getAllStates() throws Exception {
        List<State> states = countryDao.getAllStates("US");
        Assert.assertEquals(51, states.size());

        List<State> states2 = countryDao.getAllStates("TW");
        Assert.assertEquals(0, states2.size());

        List<State> states3 = countryDao.getAllStates("XXX");
        Assert.assertEquals(0, states3.size());
    }

    @Test
    public void getState() throws Exception {
        State ca = countryDao.getState("US", "CA");
        Assert.assertEquals("California", ca.getName());

        Assert.assertNull(countryDao.getState("US", "XX"));
        Assert.assertNull(countryDao.getState("XX", "YY"));
    }
}