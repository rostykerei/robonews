package nl.rostykerei.news.worker.tagger;

import nl.rostykerei.news.messaging.domain.NewStoryMessage;

/**
 * Created with IntelliJ IDEA.
 * User: rosty
 * Date: 12/23/13
 * Time: 8:12 PM
 * To change this template use File | Settings | File Templates.
 */
public class TagQueueListener {

    public void listen(NewStoryMessage message) {
        System.out.println("*****\t" + message.getTitle());
        //throw new RuntimeException("xxx");
    }
}
