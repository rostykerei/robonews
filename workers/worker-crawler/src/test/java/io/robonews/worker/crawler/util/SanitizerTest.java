/**
 * Robonews.io
 *
 * Copyright (c) 2013-2014 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.worker.crawler.util;

import io.robonews.service.syndication.SyndicationEntry;
import io.robonews.service.syndication.impl.SyndicationEntryImpl;
import org.junit.Assert;
import org.junit.Test;

import java.util.Date;

public class SanitizerTest {

    @Test(expected = SanitizerException.class)
    public void testSanitizeNull() throws SanitizerException{
        System.out.println(new Date( System.currentTimeMillis() - 60000 ));

        Sanitizer.sanitize(null);
    }

    @Test
    public void testSanitizeAuthorNull() throws SanitizerException {
        SyndicationEntry syndEntry = getFilledSyndEntry();
        syndEntry.setAuthor(null);

        Sanitizer.sanitize(syndEntry);
    }

    @Test(expected = SanitizerException.class)
    public void testSanitizeGuidNull() throws SanitizerException {
        SyndicationEntry syndEntry = getFilledSyndEntry();
        syndEntry.setGuid(null);
        Sanitizer.sanitize(syndEntry);
    }

    @Test(expected = SanitizerException.class)
    public void testSanitizeGuidLong() throws SanitizerException {
        SyndicationEntry syndEntry = getFilledSyndEntry();
        syndEntry.setGuid(LONG_STRING);
        Sanitizer.sanitize(syndEntry);
    }

    @Test(expected = SanitizerException.class)
    public void testSanitizeLinkNull() throws SanitizerException {
        SyndicationEntry syndEntry = getFilledSyndEntry();
        syndEntry.setLink(null);
        Sanitizer.sanitize(syndEntry);
    }

    @Test(expected = SanitizerException.class)
    public void testSanitizeLinkLong() throws SanitizerException {
        SyndicationEntry syndEntry = getFilledSyndEntry();
        syndEntry.setLink(LONG_STRING);
        Sanitizer.sanitize(syndEntry);
    }

    @Test(expected = SanitizerException.class)
    public void testSanitizeLinkInvalid() throws SanitizerException {
        SyndicationEntry syndEntry = getFilledSyndEntry();
        syndEntry.setLink("ftp://www.robonews.io/");
        Sanitizer.sanitize(syndEntry);
    }

    @Test(expected = SanitizerException.class)
    public void testSanitizeTitleNull() throws SanitizerException {
        SyndicationEntry syndEntry = getFilledSyndEntry();
        syndEntry.setTitle(null);
        Sanitizer.sanitize(syndEntry);
    }

    @Test(expected = SanitizerException.class)
    public void testSanitizeTitleShort() throws SanitizerException {
        SyndicationEntry syndEntry = getFilledSyndEntry();
        syndEntry.setTitle("A");
        Sanitizer.sanitize(syndEntry);
    }

    @Test
    public void testSanitizeTitleLong() throws SanitizerException {
        SyndicationEntry syndEntry = getFilledSyndEntry();
        syndEntry.setTitle(LONG_STRING + LONG_STRING + LONG_STRING);

        Assert.assertEquals(255, Sanitizer.sanitize(syndEntry).getTitle().length());
    }

    @Test(expected = SanitizerException.class)
    public void testSanitizePubDateNull() throws SanitizerException {
        SyndicationEntry syndEntry = getFilledSyndEntry();
        syndEntry.setPubDate(null);
        Sanitizer.sanitize(syndEntry);
    }

    @Test
    public void testSanitizeDescriptionLong() throws SanitizerException {
        SyndicationEntry syndEntry = getFilledSyndEntry();
        syndEntry.setDescription(LONG_STRING + LONG_STRING + LONG_STRING + LONG_STRING + LONG_STRING + LONG_STRING);

        Assert.assertEquals(1024, Sanitizer.sanitize(syndEntry).getDescription().length());
    }

    @Test
    public void testSanitize() throws SanitizerException{
        SyndicationEntry syndEntry = getFilledSyndEntry();
        syndEntry.setAuthor(" <p>Author   \n  name</p>   ");
        syndEntry.setGuid(" <value/>   should stay  be unmodifiable ");
        syndEntry.setLink("http://www.robonews.io/somepost.html");
        syndEntry.setTitle(" <div>Title</p   ");

        Date pubDate = new Date();
        syndEntry.setPubDate(pubDate);

        SyndicationEntry sanitized = Sanitizer.sanitize(syndEntry);

        Assert.assertEquals("Author name", sanitized.getAuthor());
        Assert.assertEquals(" <value/>   should stay  be unmodifiable ", sanitized.getGuid());
        Assert.assertEquals("http://www.robonews.io/somepost.html", sanitized.getLink());
        Assert.assertEquals("Title", sanitized.getTitle());
        Assert.assertEquals(pubDate, sanitized.getPubDate());
    }

    private SyndicationEntry getFilledSyndEntry() {
        SyndicationEntry syndEntry = new SyndicationEntryImpl();

        syndEntry.setDescription("Test description");
        syndEntry.setAuthor("Test author");
        syndEntry.setTitle("Test title");
        syndEntry.setLink("http://www.robonews.io/");
        syndEntry.setGuid("unique-guid");
        syndEntry.setPubDate(new Date());

        return syndEntry;
    }

    private static final String LONG_STRING = "1qazxsw23edcvfr45tgbnhy67ujmxki8" +
            "1qazxsw23edcvfr45tgbnhy67ujmxki8" +
            "1qazxsw23edcvfr45tgbnhy67ujmxki8" +
            "1qazxsw23edcvfr45tgbnhy67ujmxki8" +
            "1qazxsw23edcvfr45tgbnhy67ujmxki8" +
            "1qazxsw23edcvfr45tgbnhy67ujmxki8" +
            "1qazxsw23edcvfr45tgbnhy67ujmxki8" +
            "1qazxsw23edcvfr45tgbnhy67ujmxki8";
}
