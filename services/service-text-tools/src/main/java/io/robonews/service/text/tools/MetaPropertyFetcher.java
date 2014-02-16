/**
 * Robonews.io
 *
 * Copyright (c) 2013-2014 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.service.text.tools;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MetaPropertyFetcher {

    private static final Pattern metaPropsPattern = Pattern.compile(
            "<meta[^>]+(name|property|content)\\s*=\\s*[\"']{1}(.+?)[\"']{1}[^>]+(name|property|content)\\s*=\\s*['\"]{1}(.+?)['\"]{1}.*>",
            Pattern.CASE_INSENSITIVE);

    public static Map<String, String> fetchMetaProps(String content) {
        Map<String, String> result = new HashMap<String, String>();

        Matcher matcher = metaPropsPattern.matcher(content);

        while (matcher.find()) {

            String name1 = matcher.group(1);
            String val1 = matcher.group(2);

            String name2 = matcher.group(3);
            String val2 = matcher.group(4);

            if (("name".equalsIgnoreCase(name1) || "property".equalsIgnoreCase(name1)) && "content".equalsIgnoreCase(name2)) {
                result.put(val1.toLowerCase(), val2.trim());
            }
            else if (("name".equalsIgnoreCase(name2) || "property".equalsIgnoreCase(name2)) && "content".equalsIgnoreCase(name1)) {
                result.put(val2.toLowerCase(), val1.trim());
            }
        }

        return result;
    }

    public static Set<String> fetchMetaImages(String content) {

        Set<String> result = new HashSet<String>();

        Map<String, String> metaProps = fetchMetaProps(content);

        for (Map.Entry<String, String> entry : metaProps.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();

            if ( "og:image".equals(key) || "thumbnail_url".equals(key) ||
                    "rnews:thumbnailurl".equals(key) || "twitter:image".equals(key) ) {

                if (!result.contains(value) && UrlValidator.isValid(value)) {
                    result.add(value);
                }
            }
        }

        return result;
    }

}
