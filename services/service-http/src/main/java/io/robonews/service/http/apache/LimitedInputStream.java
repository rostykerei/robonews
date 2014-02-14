/**
 * Robonews.io
 *
 * Copyright (c) 2013-2014 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.service.http.apache;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.http.ContentTooLongException;

public class LimitedInputStream extends FilterInputStream {

    /** the wrapped input stream */
    private final InputStream in;

    /** the max length to provide */
    private final long max;

    /** the number of bytes already returned */
    private long pos = 0;

    public LimitedInputStream(InputStream in, long size) {
        super(in);
        this.max = size;
        this.in = in;
    }

    @Override
    public int read() throws IOException {
        if (pos >= max) {
            in.close();
            throw new ContentTooLongException("Content too long (more than " + max + " bytes). Stream closed.");
        }

        int result = in.read();
        pos++;
        return result;
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        if (pos>=max) {
            in.close();
            throw new ContentTooLongException("Content too long (more than " + max + " bytes). Stream closed.");
        }

        long maxRead = max>=0 ? Math.min(len, max-pos) : len;
        int bytesRead = in.read(b, off, (int)maxRead);

        if (bytesRead==-1) {
            return -1;
        }

        pos += bytesRead;
        return bytesRead;
    }

    @Override
    public long skip(long n) throws IOException {
        long toSkip = max>=0 ? Math.min(n, max-pos) : n;
        long skippedBytes = in.skip(toSkip);
        pos += skippedBytes;
        return skippedBytes;
    }
}
