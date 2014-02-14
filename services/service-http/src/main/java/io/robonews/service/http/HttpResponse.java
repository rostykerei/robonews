/**
 * Robonews.io
 *
 * Copyright (c) 2013-2014 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.service.http;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

public interface HttpResponse {

    InputStream getStream() throws IOException;

    int getHttpStatus();

    void abort();

    void releaseConnection();

    Date getLastModified();

    String getEtag();
}
