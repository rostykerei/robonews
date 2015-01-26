/**
 * Robonews.io
 *
 * Copyright (c) 2013-2015 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.service.syndication.rome;

import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.ParsingFeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;
import io.robonews.service.syndication.SyndicationFeed;
import io.robonews.service.syndication.SyndicationService;
import io.robonews.service.syndication.SyndicationServiceException;
import io.robonews.service.syndication.SyndicationServiceParsingException;

import java.io.IOException;
import java.io.InputStream;

public class SyndicationServiceRome implements SyndicationService {

    @Override
    public SyndicationFeed loadFeed(InputStream inputStream) throws SyndicationServiceException, IOException {
        try {
            SyndFeedInput syndFeedInput = new SyndFeedInput();
            syndFeedInput.setXmlHealerOn(true);

            SyndFeed syndFeed = syndFeedInput.build(new XmlReader(inputStream));
            return new SyndicationFeedRome(syndFeed);
        }
        catch (ParsingFeedException exception) {
            throw new SyndicationServiceParsingException(exception.getMessage(), exception.getCause());
        }
        catch (FeedException exception) {
            throw new SyndicationServiceException(exception.getMessage(), exception.getCause());
        }
    }
}
