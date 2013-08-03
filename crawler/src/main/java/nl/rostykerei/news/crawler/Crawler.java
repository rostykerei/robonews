package nl.rostykerei.news.crawler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;

public class Crawler {

    private Logger logger = LoggerFactory.getLogger(Crawler.class);

    @Async
    public void run() throws InterruptedException {
        logger.info("Crawler starts");
        Thread.sleep(500);
        logger.info("Crawler ends");
    }

}
