/**
 * Robonews.io
 *
 * Copyright (c) 2013-2014 Rosty Kerei.
 * All rights reserved.
 */
package nl.rostykerei.news.service.syndication;

import java.io.IOException;
import java.io.InputStream;

public interface SyndicationService {

    SyndicationFeed loadFeed(InputStream inputStream) throws SyndicationServiceException, IOException;

}
