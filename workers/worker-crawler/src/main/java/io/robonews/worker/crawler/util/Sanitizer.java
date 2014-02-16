/**
 * Robonews.io
 *
 * Copyright (c) 2013-2014 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.worker.crawler.util;

import io.robonews.service.syndication.SyndicationEntry;
import io.robonews.service.text.tools.TextSanitizer;
import io.robonews.service.text.tools.UrlValidator;

public class Sanitizer {

    public static SyndicationEntry sanitize(SyndicationEntry syndEntry) throws SanitizerException {

        if (syndEntry == null) {
            throw new SanitizerException("SyndicationEntry is null");
        }

        if (syndEntry.getAuthor() != null) {
            syndEntry.setAuthor( TextSanitizer.sanitizeText(syndEntry.getAuthor(), 255) );
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


            if (!UrlValidator.isValid(link)) {
                throw new SanitizerException("Link is not valid");
            }

        }
        else {
            throw new SanitizerException("Link is null");
        }

        if (syndEntry.getTitle() != null) {
            String title = TextSanitizer.sanitizeText(syndEntry.getTitle(), 255);

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
            syndEntry.setDescription( TextSanitizer.sanitizeText(syndEntry.getDescription(), 1024) );
        }

        return syndEntry;
    }
}
