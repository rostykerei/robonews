/**
 * Robonews.io
 *
 * Copyright (c) 2013-2014 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.service.http.apache;

import io.robonews.service.http.HttpResponse;
import org.apache.http.ContentTooLongException;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.cookie.DateParseException;
import org.apache.http.impl.cookie.DateUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

public class HttpResponseApache implements HttpResponse {

    private org.apache.http.HttpResponse response;
    private HttpGet httpGet;
    private long maxContentLength;

    public HttpResponseApache(org.apache.http.HttpResponse response, HttpGet httpGet, long maxContentLength) throws IOException {
        this.response = response;
        this.httpGet = httpGet;
        this.maxContentLength = maxContentLength;

        Header contentLengthHeader = response.getFirstHeader(HttpHeaders.CONTENT_LENGTH);

        if (contentLengthHeader != null && !StringUtils.isEmpty(contentLengthHeader.getValue())) {
            long contentLength = Long.parseLong(contentLengthHeader.getValue());

            if (contentLength > 0 && contentLength > maxContentLength) {
                throw new ContentTooLongException("Content is too long: " + contentLength +
                        " bytes expected, but only " + maxContentLength + " allowed");
            }
        }
    }

    @Override
    public InputStream getStream() throws IOException {
        HttpEntity entity = response.getEntity();

        long contentLength = entity.getContentLength();
        if (contentLength > 0 && contentLength > maxContentLength) {
            throw new ContentTooLongException("Content is too long: " + contentLength +
                    " bytes expected, but only " + maxContentLength + " allowed");
        }

        if (entity != null) {
            return new LimitedInputStream(entity.getContent(), maxContentLength);
        }

        return null;
    }

    @Override
    public int getHttpStatus() {
        return response.getStatusLine().getStatusCode();
    }

    @Override
    public void abort() {
        httpGet.abort();
    }

    @Override
    public void releaseConnection() {
        httpGet.releaseConnection();
    }

    @Override
    public Date getLastModified() {
        Header header = response.getFirstHeader(HttpHeaders.LAST_MODIFIED);

        if (header != null) {
            try {
                return DateUtils.parseDate(header.getValue());
            } catch (DateParseException e) {
                return null;
            }
        }

        return null;
    }

    @Override
    public String getEtag() {
        Header header = response.getFirstHeader(HttpHeaders.ETAG);

        if (header != null) {
            return header.getValue();
        }

        return null;
    }
}
