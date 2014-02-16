/**
 * Robonews.io
 *
 * Copyright (c) 2013-2014 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.service.text.tools;

import org.junit.Assert;
import org.junit.Test;

import java.util.Set;

public class MetaPropertyFetcherTest {

    @Test
    public void testFetchOGImage1() throws Exception {
        Set<String> urls = MetaPropertyFetcher.fetchMetaImages(
            "xxx\n<meta property=\"og:image\" content=\"http://www.robonews.io/\" />\nzzz"
        );

        Assert.assertTrue(urls.contains("http://www.robonews.io/"));
    }

    @Test
    public void testFetchOGImage2() throws Exception {
        Set<String> urls = MetaPropertyFetcher.fetchMetaImages(
                "xxx\n<meta    content=\"http://www.robonews.io/\"    property=\"og:image\">\nzzz"
        );

        Assert.assertTrue(urls.contains("http://www.robonews.io/"));
    }

    @Test
    public void testFetchOGImage3() throws Exception {
        Set<String> urls = MetaPropertyFetcher.fetchMetaImages(
                "xxx\n<META PrOPErTY='OG:ImaGE' COnTeNT=\"http://www.robonews.io/\" />\nzzz"
        );

        Assert.assertTrue(urls.contains("http://www.robonews.io/"));
    }

    @Test
    public void testFetchOGImage4() throws Exception {
        Set<String> urls = MetaPropertyFetcher.fetchMetaImages(
                "xxx\n<meta content='http://www.robonews.io/' property='thumbnail_url'/>\nzzz"
        );

        Assert.assertTrue(urls.contains("http://www.robonews.io/"));
    }

    @Test
    public void testFetchOGImage5() throws Exception {
        Set<String> urls = MetaPropertyFetcher.fetchMetaImages(
                "xxx\n<meta property= \"twitter:image\" content = 'http://www.robonews.io/'/>\nzzz"
        );

        Assert.assertTrue(urls.contains("http://www.robonews.io/"));
    }

    @Test
    public void testFetchOGImage6() throws Exception {
        Set<String> urls = MetaPropertyFetcher.fetchMetaImages(
                "xxx\n<meta\r\nproperty=\"og:image\"\ncontent=\"http://www.robonews.io/\" />\nzzz"
        );

        Assert.assertTrue(urls.contains("http://www.robonews.io/"));
    }

    @Test
    public void testFetchOGImage7() throws Exception {
        Set<String> urls = MetaPropertyFetcher.fetchMetaImages(
                "xxx\n" +
                        "<meta property=\"og:image\" content=\"http://www.google.com/\" />\n" +
                        "yyy\n" +
                        "<meta content=\"http://www.robonews.io/\" property=\"rnews:thumbnailurl\"/>\n" +
                        "zzz\n"
        );

        Assert.assertTrue(urls.contains("http://www.google.com/"));
        Assert.assertTrue(urls.contains("http://www.robonews.io/"));
    }

    @Test
    public void testFetchOGImage7b() throws Exception {
        Set<String> urls = MetaPropertyFetcher.fetchMetaImages(
                "xxx\n" +
                        "<meta property=\"og:image\" content=\"http://www.robonews.io/\" />\n" +
                        "yyy\n" +
                        "<meta content=\"http://www.robonews.io/\" property=\"rnews:thumbnailurl\"/>\n" +
                        "zzz\n"
        );

        Assert.assertTrue(urls.contains("http://www.robonews.io/"));
        Assert.assertEquals(1, urls.size());
    }

    @Test
    public void testFetchOGImage8() throws Exception {
        Set<String> urls = MetaPropertyFetcher.fetchMetaImages(
            "xxx\n<meta property=\"og:image\" con tent=\"http://www.robonews.io/\" />\nzzz"
        );

        Assert.assertEquals(0, urls.size());
    }

    @Test
    public void testFetchOGImage9() throws Exception {
        Set<String> urls = MetaPropertyFetcher.fetchMetaImages(
                "xxx\n<meta property=\"og:video\" content=\"http://www.robonews.io/\" />\nzzz"
        );

        Assert.assertEquals(0, urls.size());
    }

    @Test
    public void testFetchOGImage10() throws Exception {
        Set<String> urls = MetaPropertyFetcher.fetchMetaImages(
                "xxx\n<meta property=\"og:image\" content=\"not valid url\" />\nzzz"
        );

        Assert.assertEquals(0, urls.size());
    }

    @Test
    public void testFetchOGImage11() throws Exception {
        Set<String> urls = MetaPropertyFetcher.fetchMetaImages(
            "xxx\n<no-meta property=\"og:image\" content=\"http://www.robonews.io/\" />\nzzz"
        );

        Assert.assertEquals(0, urls.size());
    }

    @Test
    public void testFetchOGImage12() throws Exception {
        Set<String> urls = MetaPropertyFetcher.fetchMetaImages("");
        Assert.assertEquals(0, urls.size());
    }

}
