/**
 * Robonews.io
 *
 * Copyright (c) 2013-2014 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.service.http.impl;

import java.util.Date;
import io.robonews.service.http.HttpRequest;

public class HttpRequestImpl implements HttpRequest {

    private String url;

    private String lastEtag;

    private Date lastModified;

    private String accept;

    public HttpRequestImpl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLastEtag() {
        return lastEtag;
    }

    public void setLastEtag(String lastEtag) {
        this.lastEtag = lastEtag;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    @Override
    public String getAccept() {
        return accept;
    }

    @Override
    public void setAccept(String accept) {
        this.accept = accept;
    }
}
