package nl.rostykerei.news.service.http;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: rosty
 * Date: 7/25/13
 * Time: 10:47 PM
 * To change this template use File | Settings | File Templates.
 */
public interface HttpResponse {

    InputStream getStream() throws IOException;

    int getHttpStatus();

    void abort();

    void releaseConnection();

    Date getLastModified();

    String getEtag();
}
