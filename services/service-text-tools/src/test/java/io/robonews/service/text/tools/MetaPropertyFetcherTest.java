/**
 * Robonews.io
 *
 * Copyright (c) 2013-2014 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.service.text.tools;

import org.junit.Assert;
import org.junit.Test;

public class MetaPropertyFetcherTest {

    @Test
    public void testFetchOpenGraphImage() throws Exception {
        Assert.assertEquals("http://www.robonews.io/",
            MetaPropertyFetcher.fetchOpenGraphImage(
                "xxx\n<meta property=\"og:image\" content=\"http://www.robonews.io/\" />\nzzz"
            )
        );

        Assert.assertEquals("http://www.robonews.io/",
            MetaPropertyFetcher.fetchOpenGraphImage(
                "xxx\n<meta   property=\"og:image\"    content=\"http://www.robonews.io/\">\nzzz"
            )
        );

        Assert.assertEquals("http://www.robonews.io/",
            MetaPropertyFetcher.fetchOpenGraphImage(
                "xxx\n<META PrOPErTY=\"OG:ImaGE\" COnTeNT=\"http://www.robonews.io/\" />\nzzz"
            )
        );

        Assert.assertEquals("http://www.robonews.io/",
            MetaPropertyFetcher.fetchOpenGraphImage(
                "xxx\n<meta property='og:image' content='http://www.robonews.io/'/>\nzzz"
            )
        );

        Assert.assertEquals("http://www.robonews.io/",
            MetaPropertyFetcher.fetchOpenGraphImage(
                "xxx\n<meta property= \"og:image\" content = 'http://www.robonews.io/'/>\nzzz"
            )
        );

        Assert.assertEquals("http://www.robonews.io/",
            MetaPropertyFetcher.fetchOpenGraphImage(
                "xxx\n<meta\r\nproperty=\"og:image\"\ncontent=\"http://www.robonews.io/\" />\nzzz"
            )
        );

        Assert.assertEquals("http://www.robonews.io/",
            MetaPropertyFetcher.fetchOpenGraphImage(
                "xxx\n" +
                "<meta property=\"og:image\" content=\"http://www.robonews.io/\" />\n" +
                "yyy\n" +
                "<meta property=\"og:image\" content=\"http://www.google.com/\" />\n" +
                "zzz\n"
            )
        );

        Assert.assertNull(
            MetaPropertyFetcher.fetchOpenGraphImage(
                "xxx\n<meta property=\"og:image\" con tent=\"http://www.robonews.io/\" />\nzzz"
            )
        );

        Assert.assertNull(
            MetaPropertyFetcher.fetchOpenGraphImage(
                "xxx\n<meta property=\"og:video\" content=\"http://www.robonews.io/\" />\nzzz"
            )
        );

        Assert.assertNull(
            MetaPropertyFetcher.fetchOpenGraphImage(
                "xxx\n<meta property=\"og:image\" content=\"not valid url\" />\nzzz"
            )
        );

        Assert.assertNull(
            MetaPropertyFetcher.fetchOpenGraphImage(
                "xxx\n<no-meta property=\"og:image\" content=\"http://www.robonews.io/\" />\nzzz"
            )
        );

        Assert.assertNull(
            MetaPropertyFetcher.fetchOpenGraphImage(
                null
            )
        );

        Assert.assertNull(
            MetaPropertyFetcher.fetchOpenGraphImage(
                ""
            )
        );

    }
}
