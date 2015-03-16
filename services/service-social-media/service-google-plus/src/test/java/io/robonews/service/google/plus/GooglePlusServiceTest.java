/**
 * Robonews.io
 *
 * Copyright (c) 2013-2015 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.service.google.plus;

import io.robonews.service.google.plus.impl.GooglePlusServiceImpl;
import io.robonews.service.google.plus.model.GooglePlusProfile;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

public class GooglePlusServiceTest {

    GooglePlusService service;

    public GooglePlusServiceTest() throws Exception {
        service = new GooglePlusServiceImpl(
                "robonews-io-dev",
                "282775890763-vg0f6pq25uqlvq8mutl9msdis2i9aeqj@developer.gserviceaccount.com",
                new FileInputStream(
                    new File(
                             getClass()
                            .getResource("/robonews-io-dev.p12")
                            .getFile()
                    )
                )
        );
    }

    @Test
    public void test() throws Exception {
        List<GooglePlusProfile> profiles = service.searchProfiles("cnn");

        Assert.assertEquals(10, profiles.size());
        for (GooglePlusProfile profile : profiles) {
            System.out.println(profile.getName() + " " + profile.getImageUrl());
        }
    }

    @Test
    public void testProfilePic() throws Exception {
        String url = service.getProfilePictureUrl("+cnn");
    }
}