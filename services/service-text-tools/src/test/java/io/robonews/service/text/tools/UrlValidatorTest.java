/**
 * Robonews.io
 *
 * Copyright (c) 2013-2014 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.service.text.tools;

import org.junit.Assert;
import org.junit.Test;

public class UrlValidatorTest {

    @Test
    public void testIsValid() throws Exception {
        Assert.assertTrue( UrlValidator.isValid("http://robonews.io/") );
        Assert.assertTrue( UrlValidator.isValid("http://www.robonews.io/") );
        Assert.assertTrue( UrlValidator.isValid("http://www.robonews.io") );
        Assert.assertTrue( UrlValidator.isValid("http://www.robonews.io/index.html") );
        Assert.assertTrue( UrlValidator.isValid("http://www.robonews.io/ind+ex.html") );
        Assert.assertTrue( UrlValidator.isValid("http://www.robonews.io/ind%20ex.html") );
        Assert.assertTrue( UrlValidator.isValid("http://www.robonews.io/index.html?a=1&b=2;c=3") );
        Assert.assertTrue( UrlValidator.isValid("http://www.robonews.io/index.html?a=1&b=2;c=3#anchor") );

        Assert.assertFalse( UrlValidator.isValid("http://www.robonews.xyz/") );
        Assert.assertFalse( UrlValidator.isValid("https://www.robonews.io/") );
        Assert.assertFalse( UrlValidator.isValid("xwy://www.robonews.io/") );
        Assert.assertFalse( UrlValidator.isValid("www.robonews.io") );
        Assert.assertFalse( UrlValidator.isValid("http://www.robonew!s.io/") );
        Assert.assertFalse( UrlValidator.isValid("http://www.robonews.io/ind ex.html") );
    }
}
