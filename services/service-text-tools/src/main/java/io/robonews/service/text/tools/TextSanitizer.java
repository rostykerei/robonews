/**
 * Robonews.io
 *
 * Copyright (c) 2013-2014 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.service.text.tools;

import com.google.common.base.CharMatcher;
import org.jsoup.Jsoup;

public class TextSanitizer {

    private static final String BREAK_CHARACTERS = " .!,;:?-";

    private static final String ELLIPSIS = "\u2026";

    public static String sanitizeText(String text, int maxLength) {
        text = Jsoup.parse(text).text();
        text = normalizeWhitespaces(text);
        text = truncate(text, maxLength);

        return text;
    }

    public static String normalizeWhitespaces(String text) {
        return CharMatcher.WHITESPACE.collapseFrom(text, ' ').trim();
    }

    public static String truncate(String text, int maxLength) {

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

            text += ELLIPSIS;
        }

        return text;
    }
}
