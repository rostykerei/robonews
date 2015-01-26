/**
 * Robonews.io
 *
 * Copyright (c) 2013-2015 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.service.nlp.util;

import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;

public class WordCounterTest extends TestCase {

    @Test
    public void testCount() throws Exception {
        Assert.assertEquals(4, WordCounter.count("This is a test"));
        Assert.assertEquals(4, WordCounter.count("This! is, a     test"));
        Assert.assertEquals(5, WordCounter.count("This is a $254.50 test"));

        Assert.assertEquals(0, WordCounter.count(""));
        Assert.assertEquals(0, WordCounter.count(null));
    }
}