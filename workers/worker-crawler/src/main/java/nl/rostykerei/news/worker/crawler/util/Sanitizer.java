package nl.rostykerei.news.worker.crawler.util;

import com.google.common.base.CharMatcher;
import nl.rostykerei.news.service.syndication.SyndicationEntry;
import org.jsoup.Jsoup;


/**
 * Created with IntelliJ IDEA.
 * User: rosty
 * Date: 8/7/13
 * Time: 11:08 PM
 * To change this template use File | Settings | File Templates.
 */
public class Sanitizer {

    public static SyndicationEntry sanitize(SyndicationEntry syndEntry) {

        syndEntry.setAuthor( sanitizeText(syndEntry.getAuthor(), 255) );
        syndEntry.setDescription( sanitizeText(syndEntry.getDescription(), 1024) );
        syndEntry.setTitle( sanitizeText(syndEntry.getTitle(), 255) );

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
