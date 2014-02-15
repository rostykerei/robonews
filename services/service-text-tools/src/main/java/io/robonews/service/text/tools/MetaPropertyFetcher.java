/**
 * Robonews.io
 *
 * Copyright (c) 2013-2014 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.service.text.tools;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MetaPropertyFetcher {

    private static final Pattern ogImagePattern = Pattern.compile(
            "<meta[^>]+property\\s*=\\s*[\"|']og:image[\"|'][^>]+content\\s*=\\s*['|\"](.*)['|\"].*>",
            Pattern.CASE_INSENSITIVE);

    /**
     * Returns &lt;meta property="og:image"/&gt; content value
     * or null if not found;
     *
     * @param content HTML content
     * @return value of og:image meta tag
     */
    public static String fetchOpenGraphImage(String content) {
        if (content == null || content == "") {
            return null;
        }

        Matcher matcher = ogImagePattern.matcher(content);

        if (matcher.find()) {

            String url = matcher.group(1);

            if (url != null && UrlValidator.isValid(url)) {
                return url;
            }
        }

        return null;
    }

}
