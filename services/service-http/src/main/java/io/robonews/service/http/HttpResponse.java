/**
 * Robonews.io
 *
 * Copyright (c) 2013-2015 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.service.http;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

public interface HttpResponse {

    static final int STATUS_OK = 200;
    static final int STATUS_NOT_MODIFIED = 304;
    static final int STATUS_NOT_FOUND = 404;
    static final int STATUS_INTERNAL_SERVER_ERROR = 500;

    InputStream getStream() throws IOException;

    int getHttpStatus();

    void abort();

    void releaseConnection();

    Date getLastModified();

    String getEtag();

    Date getExpires();

    String getContentType();
}
