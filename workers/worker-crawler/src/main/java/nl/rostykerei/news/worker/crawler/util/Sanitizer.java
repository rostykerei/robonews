/**
 * Robonews.io
 *
 * Copyright (c) 2013-2014 Rosty Kerei.
 * All rights reserved.
 */
package nl.rostykerei.news.worker.crawler.util;

import com.google.common.base.CharMatcher;
import nl.rostykerei.news.service.syndication.SyndicationEntry;
import org.apache.commons.validator.routines.UrlValidator;
import org.jsoup.Jsoup;

public class Sanitizer {

    public static SyndicationEntry sanitize(SyndicationEntry syndEntry) throws SanitizerException {

        if (syndEntry == null) {
            throw new SanitizerException("SyndicationEntry is null");
        }

        if (syndEntry.getAuthor() != null) {
            syndEntry.setAuthor( sanitizeText(syndEntry.getAuthor(), 255));
        }

        if (syndEntry.getGuid() != null) {
            if (syndEntry.getGuid().length() > 255) {
                throw new SanitizerException("GUID is too long");
            }
        }
        else {
            throw new SanitizerException("GUID is null");
        }

        if (syndEntry.getLink() != null) {
            String link = syndEntry.getLink();

            if (link.length() > 255) {
                throw new SanitizerException("Link is too long");
            }

            UrlValidator urlValidator = new UrlValidator(new String[] {"http", "https"});

            if (!urlValidator.isValid(link)) {
                throw new SanitizerException("Link is not valid");
            }

        }
        else {
            throw new SanitizerException("Link is null");
        }

        if (syndEntry.getTitle() != null) {
            String title = sanitizeText(syndEntry.getTitle(), 255);

            if (title.length() > 4) {
                syndEntry.setTitle(title);
            }
            else {
                throw new SanitizerException("Title is too short");
            }
        }
        else {
            throw new SanitizerException("Title is null");
        }

        if (syndEntry.getPubDate() == null) {
            throw new SanitizerException("PubDate is null");
        }

        if (syndEntry.getDescription() != null) {
            syndEntry.setDescription( sanitizeText(syndEntry.getDescription(), 1024));
        }

        return syndEntry;
    }

    static String sanitizeText(String text, int maxLength) {
        text = Jsoup.parse(text).text();
        text = normalizeWhitespaces(text);
        text = truncate(text, maxLength);

        return text;
    }

    static String normalizeWhitespaces(String text) {
        return CharMatcher.WHITESPACE.collapseFrom(text, ' ').trim();
    }

    static String truncate(String text, int maxLength) {
        final String BREAK_CHARACTERS = " .!,;:?-";

        if(text.length() > maxLength)
        {
            text = text.substring(0, maxLength);

            int breakPoint = CharMatcher.anyOf(BREAK_CHARACTERS).lastIndexIn(text) + 1;

            if (breakPoint == 0) {
                text = text.substring(0, maxLength - 1);
            }
            else {
                text = text.substring(0, breakPoint);
                text = text.substring(0, CharMatcher.noneOf(BREAK_CHARACTERS).lastIndexIn(text) + 1);
            }

            text += "\u2026";
        }

        return text;
    }
}
