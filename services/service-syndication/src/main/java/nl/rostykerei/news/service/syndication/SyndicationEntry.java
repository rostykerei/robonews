package nl.rostykerei.news.service.syndication;

import java.util.Date;

public interface SyndicationEntry {

    void setTitle(String title);

    String getTitle();

    void setDescription(String description);

    String getDescription();

    void setLink(String link);

    String getLink();

    void setGuid(String guid);

    String getGuid();

    void setAuthor(String author);

    String getAuthor();

    void setPubDate(Date pubDate);

    Date getPubDate();

}
