/**
 * Robonews.io
 *
 * Copyright (c) 2013-2014 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.service.http.apache;

import io.robonews.service.http.HttpResponse;
import io.robonews.service.http.HttpService;
import java.io.IOException;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import io.robonews.service.http.HttpRequest;
import org.apache.http.HttpException;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.ResponseContentEncoding;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.cookie.DateUtils;
import org.apache.http.protocol.HttpContext;
import org.springframework.util.StringUtils;

public class HttpServiceApache implements HttpService {

    private AbstractHttpClient httpClient;

    private String userAgent = "RssBot";

    private int timeout = 5 * 1000;

    private long maxContentLength = 256 * 1024;

    public HttpServiceApache(AbstractHttpClient httpClient) {
        this.httpClient = httpClient;

        httpClient.getParams().setParameter("http.socket.timeout", getTimeout());
        httpClient.getParams().setParameter("http.connection.timeout", getTimeout());
        httpClient.getParams().setParameter("http.connection-manager.timeout", new Long(getTimeout()));
        httpClient.getParams().setParameter("http.protocol.head-body-timeout", getTimeout());

        this.httpClient.addRequestInterceptor(new HttpRequestInterceptor() {
            @Override
            public void process(org.apache.http.HttpRequest request, HttpContext context) throws HttpException, IOException {
                request.setHeader(HttpHeaders.USER_AGENT, getUserAgent());
                request.setHeader(HttpHeaders.ACCEPT_ENCODING, "gzip,deflate");
                request.setHeader(HttpHeaders.CACHE_CONTROL, "public");
                request.setHeader(HttpHeaders.CONNECTION, "close");
            }
        });

        this.httpClient.addResponseInterceptor(new ResponseContentEncoding());
    }

    @Override
    public HttpResponse execute(HttpRequest httpRequest) throws IOException {
        final HttpGet httpGet = new HttpGet(httpRequest.getUrl());

        String accept = httpRequest.getAccept();
        String etag = httpRequest.getLastEtag();
        Date lastModified = httpRequest.getLastModified();

        if (!StringUtils.isEmpty(accept)) {
            httpGet.setHeader(HttpHeaders.ACCEPT, accept);
        }

        if (!StringUtils.isEmpty(etag)) {
            httpGet.setHeader(HttpHeaders.IF_NONE_MATCH, etag);
        }

        if (!StringUtils.isEmpty(lastModified)) {
            String ifModifiedSinceString = DateUtils.formatDate(lastModified, "EEE, d MMM yyyy HH:mm:ss 'GMT'");
            httpGet.setHeader(HttpHeaders.IF_MODIFIED_SINCE, ifModifiedSinceString );
        }

        final org.apache.http.HttpResponse httpResponse = httpClient.execute(httpGet);

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                synchronized (this) {
                    if(httpGet != null) {
                        httpGet.abort();
                    }
                }
            }
        }, getTimeout());

        return new HttpResponseApache(httpResponse, httpGet, getMaxContentLength());
    }

    @Override
    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    @Override
    public String getUserAgent() {
        return userAgent;
    }

    @Override
    public void setTimeout(int milliseconds) {
        this.timeout = milliseconds;
    }

    @Override
    public int getTimeout() {
        return timeout;
    }

    @Override
    public void setMaxContentLength(long bytes) {
        this.maxContentLength = bytes;
    }

    @Override
    public long getMaxContentLength() {
        return maxContentLength;
    }
}
