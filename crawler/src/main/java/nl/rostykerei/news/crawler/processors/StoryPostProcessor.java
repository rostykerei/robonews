package nl.rostykerei.news.crawler.processors;

import nl.rostykerei.news.domain.Story;

/**
 * Created with IntelliJ IDEA.
 * User: rosty
 * Date: 8/7/13
 * Time: 10:46 PM
 * To change this template use File | Settings | File Templates.
 */
public interface StoryPostProcessor {

    Story postProcess(Story story);

}
