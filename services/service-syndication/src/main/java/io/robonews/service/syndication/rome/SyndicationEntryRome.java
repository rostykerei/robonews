/**
 * Robonews.io
 *
 * Copyright (c) 2013-2014 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.service.syndication.rome;

import com.sun.syndication.feed.synd.SyndEnclosure;
import com.sun.syndication.feed.synd.SyndEntry;
import io.robonews.service.syndication.SyndicationEntry;
import org.apache.commons.validator.routines.UrlValidator;
import org.jdom.Attribute;
import org.jdom.Element;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SyndicationEntryRome implements SyndicationEntry {

    private SyndEntry syndEntry;

    private Set<String> mediaKeywords = new HashSet<String>();
    private Set<String> mediaImages = new HashSet<String>();

    private static UrlValidator urlValidator = new UrlValidator(new String[] {"http"});

    public SyndicationEntryRome(SyndEntry syndEntry) {
        this.syndEntry = syndEntry;

        try {
            processEnclosures(syndEntry);
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

    @Override
    public void setMediaImages(Set<String> urls) {
        this.mediaImages = urls;
    }

    @Override
    public Set<String> getMediaImages() {
        return mediaImages;
    }

    private void processEnclosures(SyndEntry syndEntry) {
        if (syndEntry.getEnclosures() == null || syndEntry.getEnclosures().size() < 1) {
            return;
        }

        List enclosures = syndEntry.getEnclosures();

        for (Object oEnclosure : enclosures) {
            SyndEnclosure enclosure = (SyndEnclosure) oEnclosure;

            if (enclosure == null) {
                continue;
            }

            String imageType = enclosure.getType();
            String imageUrl = enclosure.getUrl();

            if (isImageMime(imageType) && imageUrl != null && imageUrl.length() < 256 && urlValidator.isValid(imageUrl)) {
                mediaImages.add(imageUrl);
            }
        }
    }

    private boolean isImageMime(String type) {
        if (type == null || type.length() < 9) {
            return false;
        }

        type = type.toLowerCase();

        if ("image/jpg".equals(type)) {
            return true;
        }

        if ("image/png".equals(type)) {
            return true;
        }

        if ("image/gif".equals(type)) {
            return true;
        }

        if ("image/jpeg".equals(type)) {
            return true;
        }

        if ("image/pjpeg".equals(type)) {
            return true;
        }

        return false;
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

                if ("content".equalsIgnoreCase(element.getName())) {
                    processMediaContent(element);
                }
            }
        }
    }

    private void processMediaContent(Element element) {
        if (element == null) {
            return;
        }

        Attribute typeAttribute = element.getAttribute("type");
        Attribute urlAttribute = element.getAttribute("url");

        if (typeAttribute != null &&
                urlAttribute != null &&
                isImageMime(typeAttribute.getValue()) &&
                urlAttribute.getValue().length() < 256 &&
                urlValidator.isValid(urlAttribute.getValue())) {
            mediaImages.add(urlAttribute.getValue());
        }
    }

    private void processMediaKeywords(String keywords) {
        if (keywords == null || keywords.length() == 0) {
            return;
        }

        String[] l = keywords.split(",");

        for (String kw : l) {
            if (kw != null && kw.length() > 0 && kw.length() < 64) {
                mediaKeywords.add(kw.trim());
            }
        }
    }

}
