package nl.rostykerei.news.service.http.apache;

import nl.rostykerei.news.service.http.HttpResponse;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpGet;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created with IntelliJ IDEA.
 * User: rosty
 * Date: 7/25/13
 * Time: 11:51 PM
 * To change this template use File | Settings | File Templates.
 */
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
    public void abort() {
        httpGet.abort();
    }

    @Override
    public void releaseConnection() {
        httpGet.releaseConnection();
    }
}
