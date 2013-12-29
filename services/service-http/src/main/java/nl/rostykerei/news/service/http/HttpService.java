package nl.rostykerei.news.service.http;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: rosty
 * Date: 7/25/13
 * Time: 9:54 PM
 * To change this template use File | Settings | File Templates.
 */
public interface HttpService {

    HttpResponse execute(HttpRequest httpRequest) throws IOException;

    void setUserAgent(String userAgent);

    String getUserAgent();

    void setTimeout(int milliseconds);

    int getTimeout();

    void setMaxContentLength(long bytes);

    long getMaxContentLength();
}
