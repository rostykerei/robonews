/**
 * Robonews.io
 *
 * Copyright (c) 2013-2015 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.service.twitter;

import io.robonews.service.twitter.impl.TwitterServiceImpl;
import io.robonews.service.twitter.model.TwitterProfile;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@ContextConfiguration({ "classpath:testContext.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
public class TwitterServiceTest {

    @Autowired
    TwitterService twitterService;

    @Test
    public void test1() {

        List<TwitterProfile> res = twitterService.searchProfiles("cnn");
        System.out.println(res);
    }

}