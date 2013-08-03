package nl.rostykerei.news.service.http;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: rosty
 * Date: 7/25/13
 * Time: 11:57 PM
 * To change this template use File | Settings | File Templates.
 */
public interface HttpRequest {

    String getUrl();

    String getLastEtag();

    Date getLastModified();
}
