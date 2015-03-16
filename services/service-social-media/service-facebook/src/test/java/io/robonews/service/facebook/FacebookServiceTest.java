/**
 * Robonews.io
 *
 * Copyright (c) 2013-2015 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.service.facebook;

import io.robonews.service.facebook.impl.FacebookServiceImpl;
import io.robonews.service.facebook.model.FacebookProfile;
import java.util.List;
import org.junit.Test;

public class FacebookServiceTest {

    FacebookService service = new FacebookServiceImpl("755872917839699|e0b73d5bfab1f106079ff8691d8fc431");

    @Test
    public void fbTest() {

        List<FacebookProfile> results = service.searchProfiles("cnn");

        System.out.println("Test");
    }

    @Test
    public void test2() {
        String url = service.getProfilePictureUrl("Cdsf3rfeNN");

        System.out.println(url);
    }

}
