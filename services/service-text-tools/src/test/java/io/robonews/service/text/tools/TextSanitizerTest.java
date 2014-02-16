/**
 * Robonews.io
 *
 * Copyright (c) 2013-2014 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.service.text.tools;

import org.junit.Assert;
import org.junit.Test;

public class TextSanitizerTest {

    @Test
    public void testSanitizeText() {
        Assert.assertEquals("Test", TextSanitizer.sanitizeText("Test", 255));
        Assert.assertEquals("Test", TextSanitizer.sanitizeText("<h1>Test", 255));
        Assert.assertEquals("Test", TextSanitizer.sanitizeText("<bad>Test", 255));
        Assert.assertEquals("Test", TextSanitizer.sanitizeText("<b class=\"x\">Test</b>    ", 255));
        Assert.assertEquals("Test", TextSanitizer.sanitizeText("<h1>Test</h1>", 255));

    }

    @Test
    public void testNormalizeWhitespaces() {
        Assert.assertEquals("Test", TextSanitizer.normalizeWhitespaces("  Test"));
        Assert.assertEquals("Test", TextSanitizer.normalizeWhitespaces("Test   "));
        Assert.assertEquals("Te st", TextSanitizer.normalizeWhitespaces("Te   st"));
        Assert.assertEquals("T e s t", TextSanitizer.normalizeWhitespaces("  T \u200a e \t  s\n     t   "));
    }

    @Test
    public void testTruncate() {
        Assert.assertEquals("abc xyz…", TextSanitizer.truncate("abc xyz qwe 123 ope", 10));
        Assert.assertEquals("abc xyz…", TextSanitizer.truncate("abc xyz, qwe 123 ope", 10));
        Assert.assertEquals("abc xyz…", TextSanitizer.truncate("abc xyz! Qwe 123 ope", 10));
        Assert.assertEquals("abc xyz q…", TextSanitizer.truncate("abc xyz q? safdsf", 10));
        Assert.assertEquals("123456789…", TextSanitizer.truncate("12345678901234567", 10));
    }
}
