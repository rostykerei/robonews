/**
 * Robonews.io
 *
 * Copyright (c) 2013-2014 Rosty Kerei.
 * All rights reserved.
 */
package nl.rostykerei.news.service.syndication;

import java.util.List;

public interface SyndicationFeed {

    String getTitle();

    String getDescription();

    String getLink();

    String getCopyright();

    String getAuthor();

    String getImageUrl();

    List<SyndicationEntry> getEntries();

    double estimateVelocity();
}
