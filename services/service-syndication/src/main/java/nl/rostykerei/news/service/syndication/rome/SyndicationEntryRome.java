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
    public void setTitle(String title) {
        syndEntry.setTitle(title);
    }

    @Override
    public String getTitle() {
        return syndEntry.getTitle();
    }

    @Override
    public void setDescription(String description) {
        syndEntry.getDescription().setValue(description);
    }

    @Override
    public String getDescription() {
        return syndEntry.getDescription().getValue();
    }

    @Override
    public void setLink(String link) {
        syndEntry.setLink(link);
    }

    @Override
    public String getLink() {
        return syndEntry.getLink();
    }

    @Override
    public void setGuid(String guid) {
        syndEntry.setUri(guid);
    }

    @Override
    public String getGuid() {
        return syndEntry.getUri();
    }

    @Override
    public void setAuthor(String author) {
        syndEntry.setAuthor(author);
    }

    @Override
    public String getAuthor() {
        return syndEntry.getAuthor();
    }

    @Override
    public void setPubDate(Date pubDate) {
        syndEntry.setPublishedDate(pubDate);
    }

    @Override
    public Date getPubDate() {
        return syndEntry.getPublishedDate();
    }
}
