package nl.rostykerei.news.service.http.apache;

import nl.rostykerei.news.service.http.HttpResponse;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.cookie.DateParseException;
import org.apache.http.impl.cookie.DateUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

public class HttpResponseApache implements HttpResponse {

    private org.apache.http.HttpResponse response;
    private HttpGet httpGet;

    public HttpResponseApache(org.apache.http.HttpResponse response, HttpGet httpGet) {
        this.response = response;
        this.httpGet = httpGet;
    }

    @Override
    public InputStream getStream() throws IOException {
        HttpEntity entity = response.getEntity();

        if (entity != null) {
            return entity.getContent();
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
