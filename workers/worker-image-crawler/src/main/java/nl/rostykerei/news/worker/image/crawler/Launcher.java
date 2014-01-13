package nl.rostykerei.news.worker.image.crawler;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created with IntelliJ IDEA.
 * User: rosty
 * Date: 1/13/14
 * Time: 7:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class Launcher {

    public static void main(String... args) {
        AbstractApplicationContext ctx =
                new ClassPathXmlApplicationContext("workerImageCrawlerContext.xml");
    }
}
