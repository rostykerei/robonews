package nl.rostykerei.news.service.syndication;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created with IntelliJ IDEA.
 * User: rosty
 * Date: 7/25/13
 * Time: 9:53 PM
 * To change this template use File | Settings | File Templates.
 */
public interface SyndicationService {

    SyndicationFeed loadFeed(InputStream inputStream) throws SyndicationServiceException, IOException;

}
