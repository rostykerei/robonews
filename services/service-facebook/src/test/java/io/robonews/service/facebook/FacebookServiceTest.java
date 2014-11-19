/**
 * Robonews.io
 *
 * Copyright (c) 2013-2014 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.service.facebook;

import io.robonews.service.facebook.impl.FacebookServiceImpl;
import io.robonews.service.facebook.model.Page;
import java.util.List;
import org.junit.Test;

public class FacebookServiceTest {

    @Test
    public void fbTest() {
        FacebookService service = new FacebookServiceImpl("755872917839699|e0b73d5bfab1f106079ff8691d8fc431");

        List<Page> results = service.searchPage("cnn");

        System.out.println("Test");
    }

    @Test
    public void test2() {
        String in = "{\"data\":{\"url\":\"https://fbcdn-profile-a.akamaihd.net/hprofile-ak-xaf1/v/t1.0-1/p50x50/1931201_165269726508_5805824_n.jpg?oh=71eaabdd882ff52ab6a77943c9310db7&oe=54DBE034&__gda__=1424045523_74e970e3a153a5422eb6c99afbba0b93\"}}";



    }
}
