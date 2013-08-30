package nl.rostykerei.news.service.syndication.rome;

import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.ParsingFeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;
import java.io.IOException;
import java.io.InputStream;
import nl.rostykerei.news.service.syndication.SyndicationFeed;
import nl.rostykerei.news.service.syndication.SyndicationService;
import nl.rostykerei.news.service.syndication.SyndicationServiceException;
import nl.rostykerei.news.service.syndication.SyndicationServiceParsingException;

/**
 * Created with IntelliJ IDEA.
 * User: rosty
 * Date: 7/25/13
 * Time: 9:55 PM
 * To change this template use File | Settings | File Templates.
 */
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
