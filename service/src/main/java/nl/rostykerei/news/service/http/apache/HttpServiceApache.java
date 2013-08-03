package nl.rostykerei.news.service.http.apache;

import nl.rostykerei.news.service.http.HttpRequest;
import nl.rostykerei.news.service.http.HttpResponse;
import nl.rostykerei.news.service.http.HttpService;
import org.apache.http.HttpHeaders;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.cookie.DateUtils;

import java.io.IOException;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: rosty
 * Date: 7/25/13
 * Time: 10:39 PM
 * To change this template use File | Settings | File Templates.
 */
public class HttpServiceApache implements HttpService {

    private HttpClient httpClient;

    public HttpServiceApache(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    @Override
    public HttpResponse execute(HttpRequest httpRequest) throws IOException {
        HttpGet httpGet = new HttpGet(httpRequest.getUrl());

        String etag = httpRequest.getLastEtag();
        Date lastModified = httpRequest.getLastModified();

        if (etag != null) {
            httpGet.setHeader(HttpHeaders.IF_NONE_MATCH,etag);
        }

        if (lastModified != null) {
            String ifModifiedSinceString = DateUtils.formatDate(lastModified, "EEE, d MMM yyyy HH:mm:ss 'GMT'");
            httpGet.setHeader(HttpHeaders.IF_MODIFIED_SINCE, ifModifiedSinceString );
        }

        /*
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                if(httpResponse != null) {
                    httpResponse.abort();
                }
            }
        }, 10000); */

        return new HttpResponseApache(httpClient.execute(httpGet), httpGet);
    }
}
