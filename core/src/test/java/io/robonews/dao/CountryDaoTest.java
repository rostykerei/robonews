package io.robonews.dao;

import io.robonews.domain.Country;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.junit.Assert;

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
    public void testGetAll() throws Exception {
        List<Country> list = countryDao.getAll();

        Assert.assertTrue(list.size() > 200);
    }
}