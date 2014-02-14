/**
 * Robonews.io
 *
 * Copyright (c) 2013-2014 Rosty Kerei.
 * All rights reserved.
 */
package nl.rostykerei.news.service.syndication;

import java.util.Date;
import java.util.Set;

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

    void setMediaKeywords(Set<String> keywords);

    Set<String> getMediaKeywords();

    void setMediaImages(Set<String> urls);

    Set<String> getMediaImages();

}
