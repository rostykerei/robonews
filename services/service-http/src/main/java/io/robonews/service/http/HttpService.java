/**
 * Robonews.io
 *
 * Copyright (c) 2013-2015 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.service.http;

import java.io.IOException;

public interface HttpService {

    HttpResponse execute(HttpRequest httpRequest) throws IOException;

    void setMaxContentLength(long bytes);

    long getMaxContentLength();
}
