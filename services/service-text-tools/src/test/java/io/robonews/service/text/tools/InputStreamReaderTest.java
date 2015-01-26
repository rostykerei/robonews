/**
 * Robonews.io
 *
 * Copyright (c) 2013-2015 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.service.text.tools;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.InputStream;

public class InputStreamReaderTest {

    @Test
    public void testRead() throws Exception {
        String source = "This is the source of my input stream";
        InputStream in = IOUtils.toInputStream(source, "UTF-8");

        Assert.assertEquals(source, InputStreamReader.read(in));
    }
}
