package nl.rostykerei.news.service.syndication.rome;

import com.sun.syndication.feed.synd.SyndEntry;
import java.util.Date;
import nl.rostykerei.news.service.syndication.SyndicationEntry;

public class SyndicationEntryRome implements SyndicationEntry {

    private SyndEntry syndEntry;

    public SyndicationEntryRome(SyndEntry syndEntry) {
        this.syndEntry = syndEntry;
    }

    @Override
    public String getTitle() {
        return syndEntry.getTitle();
    }

    @Override
    public String getDescription() {
        return syndEntry.getDescription().getValue();
    }

    @Override
    public String getLink() {
        return syndEntry.getLink();
    }

    @Override
    public String getGuid() {
        return syndEntry.getUri();
    }

    @Override
    public String getAuthor() {
        return syndEntry.getAuthor();
    }

    @Override
    public Date getPubDate() {
        return syndEntry.getPublishedDate();
    }
}
