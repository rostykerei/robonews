package io.robonews.service.text.tools;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by rosty on 04/05/15.
 */
public class LinkTagFetcher {

    private static final Pattern linkTagPattern = Pattern.compile(
            "<link[^>]+(rel|href)\\s*=\\s*[\"']{1}(.+?)[\"']{1}[^>]+(rel|href)\\s*=\\s*['\"]{1}(.+?)['\"]{1}.*>",
            Pattern.CASE_INSENSITIVE);

    public static Map<String, String> fetchLinkTags(String content) {
        Map<String, String> result = new HashMap<String, String>();

        Matcher matcher = linkTagPattern.matcher(content);

        while (matcher.find()) {

            String name1 = matcher.group(1);
            String val1 = matcher.group(2);

            String name2 = matcher.group(3);
            String val2 = matcher.group(4);

            if ("rel".equalsIgnoreCase(name1) && "href".equalsIgnoreCase(name2)) {
                result.put(val1.toLowerCase(), val2.trim());
            }
            else if ("rel".equalsIgnoreCase(name2) && "href".equalsIgnoreCase(name1)) {
                result.put(val2.toLowerCase(), val1.trim());
            }
        }

        return result;
    }

}
