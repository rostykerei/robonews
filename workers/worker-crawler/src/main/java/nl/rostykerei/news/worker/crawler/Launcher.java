package nl.rostykerei.news.worker.crawler;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Launcher {

    public static void main(String... args) {
        AbstractApplicationContext ctx =
                new ClassPathXmlApplicationContext("workerCrawlerContext.xml");
    }
}
