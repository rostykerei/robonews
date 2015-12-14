/**
 * Robonews.io
 *
 * Copyright (c) 2013-2015 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.service.syndication.rome;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import io.robonews.service.syndication.SyndicationEntry;
import io.robonews.service.syndication.SyndicationFeed;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class SyndicationFeedRome implements SyndicationFeed {

    private String title;

    private String description;

    private String link;

    private String copyright;

    private String author;

    private String imageUrl;

    private List<SyndicationEntry> entries;

    public SyndicationFeedRome(SyndFeed syndFeed) {

        if (syndFeed.getTitle() != null) {
            title = syndFeed.getTitle().trim();
        }

        if (syndFeed.getDescription() != null) {
            description = syndFeed.getDescription().trim();
        }

        if (syndFeed.getLink() != null) {
            link = syndFeed.getLink().trim();
        }

        if (syndFeed.getCopyright() != null) {
            copyright = syndFeed.getCopyright().trim();
        }

        if (syndFeed.getAuthor() != null) {
            author = syndFeed.getAuthor().trim();
        }

        if (syndFeed.getImage() != null && syndFeed.getImage().getUrl() != null) {
            imageUrl = syndFeed.getImage().getUrl().trim();
        }

        entries = new ArrayList<>();

        Iterator iterator = syndFeed.getEntries().iterator();
        while (iterator.hasNext()) {
            SyndEntry syndEntry = (SyndEntry) iterator.next();

            SyndicationEntry entry = new SyndicationEntryRome(syndEntry);

            if (StringUtils.isEmpty(entry.getTitle())) {
                continue;
            }

            if (StringUtils.isEmpty(entry.getLink())) {
                continue;
            }

            if (StringUtils.isEmpty(entry.getGuid())) {
                continue;
            }

            if (entry.getPubDate() == null) {
                continue;
            }

            entries.add(entry);
        }
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getLink() {
        return link;
    }

    @Override
    public String getCopyright() {
        return copyright;
    }

    @Override
    public String getAuthor() {
        return author;
    }

    @Override
    public String getImageUrl() {
        return imageUrl;
    }

    @Override
    public List<SyndicationEntry> getEntries() {
        return entries;
    }

    @Override
    public double estimateVelocity() {
        int size = getEntries().size();
        if (size < 2) {
            return 0;
        }

        long minDate = Long.MAX_VALUE;
        long maxDate = 0;

        Iterator iterator = getEntries().iterator();
        for (SyndicationEntry entry : getEntries()) {
            long pubDate = entry.getPubDate().getTime();

            minDate = pubDate < minDate ? pubDate : minDate;
            maxDate = pubDate > maxDate ? pubDate : maxDate;
        }

        long dateDiff = maxDate - minDate;

        if (dateDiff > 0) {
            return (size * 3600000L) / (double) dateDiff;
        }

        return 0;
    }
}
