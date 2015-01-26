/**
 * Robonews.io
 *
 * Copyright (c) 2013-2015 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.service.http;

import java.util.Date;

public interface HttpRequest {

    String getUrl();

    void setUrl(String url);

    String getLastEtag();

    void setLastEtag(String lastEtag);

    Date getLastModified();

    void setLastModified(Date lastModified);

    String getAccept();

    void setAccept(String accept);
}
