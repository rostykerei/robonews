package nl.rostykerei.news.crawler.processors.post;

import nl.rostykerei.news.crawler.processors.StoryPostProcessor;
import nl.rostykerei.news.domain.Story;

/**
 * Created with IntelliJ IDEA.
 * User: rosty
 * Date: 8/7/13
 * Time: 11:07 PM
 * To change this template use File | Settings | File Templates.
 */
public class FetchImages implements StoryPostProcessor {

    @Override
    public Story postProcess(Story story) {
        return story;
    }
}
