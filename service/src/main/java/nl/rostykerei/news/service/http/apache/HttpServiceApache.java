package nl.rostykerei.news.service.http.apache;

import java.io.IOException;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import nl.rostykerei.news.service.http.HttpRequest;
import nl.rostykerei.news.service.http.HttpResponse;
import nl.rostykerei.news.service.http.HttpService;
import org.apache.http.HttpException;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.ResponseContentEncoding;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.cookie.DateUtils;
import org.apache.http.protocol.HttpContext;

/**
 * Created with IntelliJ IDEA.
 * User: rosty
 * Date: 7/25/13
 * Time: 10:39 PM
 * To change this template use File | Settings | File Templates.
 */
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
                request.setHeader(HttpHeaders.ACCEPT, "application/rss+xml," +
                        "application/rdf+xml," +
                        "application/atom+xml," +
                        "application/xml,text/xml");
            }
        });

        this.httpClient.addResponseInterceptor(new ResponseContentEncoding());
    }

    @Override
    public HttpResponse execute(HttpRequest httpRequest) throws IOException {
        final HttpGet httpGet = new HttpGet(httpRequest.getUrl());

        String etag = httpRequest.getLastEtag();
        Date lastModified = httpRequest.getLastModified();

        if (etag != null) {
            httpGet.setHeader(HttpHeaders.IF_NONE_MATCH, etag);
        }

        if (lastModified != null) {
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
