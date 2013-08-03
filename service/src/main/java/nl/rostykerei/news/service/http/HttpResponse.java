package nl.rostykerei.news.service.http;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created with IntelliJ IDEA.
 * User: rosty
 * Date: 7/25/13
 * Time: 10:47 PM
 * To change this template use File | Settings | File Templates.
 */
public interface HttpResponse {

    InputStream getStream() throws IOException;

    void abort();

    void releaseConnection();


}
