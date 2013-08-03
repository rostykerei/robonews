package nl.rostykerei.news.service.syndication;

import java.util.Date;

public interface SyndicationEntry {

    String getTitle();

    String getDescription();

    String getLink();

    String getGuid();

    String getAuthor();

    Date getPubDate();

}
