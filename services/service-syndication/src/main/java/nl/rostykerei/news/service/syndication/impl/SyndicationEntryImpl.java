package nl.rostykerei.news.service.syndication.impl;

import java.util.Date;
import nl.rostykerei.news.service.syndication.SyndicationEntry;

/**
 * Created with IntelliJ IDEA on 9/3/13 at 12:27 PM
 *
 * @author Rosty Kerei
 */
public class SyndicationEntryImpl implements SyndicationEntry {

    private String title;
    private String description;
    private String link;
    private String guid;
    private String author;
    private Date pubDate;

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setLink(String link) {
        this.link = link;
    }

    @Override
    public String getLink() {
        return link;
    }

    @Override
    public void setGuid(String guid) {
        this.guid = guid;
    }

    @Override
    public String getGuid() {
        return guid;
    }

    @Override
    public void setAuthor(String author) {
        this.author = author;
    }

    @Override
    public String getAuthor() {
        return author;
    }

    @Override
    public void setPubDate(Date pubDate) {
        this.pubDate = pubDate;
    }

    @Override
    public Date getPubDate() {
        return pubDate;
    }
}
