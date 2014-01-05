package nl.rostykerei.news.service.syndication.rome;

import com.sun.syndication.feed.synd.SyndEntry;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import nl.rostykerei.news.service.syndication.SyndicationEntry;
import org.jdom.Element;

public class SyndicationEntryRome implements SyndicationEntry {

    private SyndEntry syndEntry;

    private Set<String> mediaKeywords = new HashSet<String>();

    public SyndicationEntryRome(SyndEntry syndEntry) {
        this.syndEntry = syndEntry;

        try {
            processForeignMarkup(syndEntry);
        }
        catch (RuntimeException e) {
            // Do nothing
        }
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

    @Override
    public void setMediaKeywords(Set<String> keywords) {
        this.mediaKeywords = keywords;
    }

    @Override
    public Set<String> getMediaKeywords() {
        return mediaKeywords;
    }

    private void processForeignMarkup(SyndEntry syndEntry) {
        if (syndEntry.getForeignMarkup() == null) {
            return;
        }

        List foreignMarkup = (List) syndEntry.getForeignMarkup();

        if (foreignMarkup == null || foreignMarkup.size() == 0) {
            return;
        }

        for (Object aForeignMarkup : foreignMarkup) {
            Element element = (Element) aForeignMarkup;

            if (element == null) {
                continue;
            }

            if ("media".equalsIgnoreCase(element.getNamespacePrefix())) {

                if ("keywords".equalsIgnoreCase(element.getName())) {
                    processMediaKeywords(element.getValue());
                }

            }
        }
    }

    private void processMediaKeywords(String keywords) {
        if (keywords == null || keywords.length() == 0) {
            return;
        }

        String[] l = keywords.split(",");

        for (String kw : l) {
            mediaKeywords.add(kw.trim());
        }
    }

}
