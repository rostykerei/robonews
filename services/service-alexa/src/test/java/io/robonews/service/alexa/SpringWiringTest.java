/**
 * Robonews.io
 *
 * Copyright (c) 2013-2014 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.service.alexa;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

@ContextConfiguration({ "classpath:testContext.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
public class SpringWiringTest {

    @Autowired
    private AlexaService springAlexaService;

    @Test
    public void testSpringWiring() throws Exception {
        Assert.notNull(springAlexaService);
    }
}
