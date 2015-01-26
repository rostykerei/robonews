/**
 * Robonews.io
 *
 * Copyright (c) 2013-2015 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.service.text.tools;

import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

public class InputStreamReader {

    public static String read(InputStream inputStream) throws IOException {
        return read(inputStream, Charsets.UTF_8);
    }

    public static String read(InputStream inputStream, Charset encoding) throws IOException {
        return IOUtils.toString(inputStream, encoding);
    }
}
