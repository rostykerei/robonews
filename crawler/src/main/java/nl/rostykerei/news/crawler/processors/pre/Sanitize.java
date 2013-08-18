package nl.rostykerei.news.crawler.processors.pre;

import com.google.common.base.CharMatcher;
import nl.rostykerei.news.crawler.processors.StoryPreProcessor;
import nl.rostykerei.news.service.syndication.SyndicationEntry;
import org.jsoup.Jsoup;


/**
 * Created with IntelliJ IDEA.
 * User: rosty
 * Date: 8/7/13
 * Time: 11:08 PM
 * To change this template use File | Settings | File Templates.
 */
public class Sanitize implements StoryPreProcessor {

    @Override
    public SyndicationEntry preProcess(SyndicationEntry syndEntry) {
        String txt = syndEntry.getDescription();

        txt = Jsoup.parse(txt).text();

        txt = CharMatcher.WHITESPACE.replaceFrom(txt, " ");
        txt = txt.trim();

        syndEntry.setDescription(txt);

        return syndEntry;
    }

    public static String normalizeWhitespaces(String text) {
        return CharMatcher.WHITESPACE.collapseFrom(text, ' ').trim();
    }

    public static String truncate(String text, int maxLength) {
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
