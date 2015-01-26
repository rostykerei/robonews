/**
 * Robonews.io
 *
 * Copyright (c) 2013-2015 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.service.freebase;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@ContextConfiguration({ "classpath:testContext.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
public class FreebaseServiceTest {

    @Autowired
    private FreebaseService freebaseService;

    @Test
    public void testSearchForPerson() throws Exception {
        Assert.assertEquals("Barack Obama", freebaseService.searchPerson("Obama").getName());
        Assert.assertEquals("Vladimir Putin", freebaseService.searchPerson("Putin").getName());
    }

    @Test
    public void testSearchForLocation() throws Exception {
        Assert.assertEquals("United States of America", freebaseService.searchLocation("U.S.").getName());
    }

    @Test
    public void testSearchForOrganization() throws Exception {
        Assert.assertEquals("Transportation Security Administration", freebaseService.searchOrganization("TSA").getName());
    }

    @Test
    public void testSearchForMiscellaneous() throws Exception {
        Assert.assertEquals("Olympic Games", freebaseService.searchMisc("Olympic").getName());
    }
}
