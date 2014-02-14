/**
 * Robonews.io
 *
 * Copyright (c) 2013-2014 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.service.http;

import java.io.IOException;

public interface HttpService {

    HttpResponse execute(HttpRequest httpRequest) throws IOException;

    void setUserAgent(String userAgent);

    String getUserAgent();

    void setTimeout(int milliseconds);

    int getTimeout();

    void setMaxContentLength(long bytes);

    long getMaxContentLength();
}
