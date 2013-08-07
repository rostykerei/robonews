package nl.rostykerei.news.crawler.processors.pre;

import nl.rostykerei.news.crawler.processors.StoryPreProcessor;
import nl.rostykerei.news.service.syndication.SyndicationEntry;

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
        return syndEntry;
    }
}
