package nl.rostykerei.news.crawler.processors;

import nl.rostykerei.news.service.syndication.SyndicationEntry;

/**
 * Created with IntelliJ IDEA.
 * User: rosty
 * Date: 8/7/13
 * Time: 10:47 PM
 * To change this template use File | Settings | File Templates.
 */
public interface StoryPreProcessor {

    SyndicationEntry preProcess(SyndicationEntry syndEntry);
}
