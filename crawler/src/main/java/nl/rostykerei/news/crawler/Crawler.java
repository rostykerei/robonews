package nl.rostykerei.news.crawler;

import nl.rostykerei.news.dao.FeedDao;
import nl.rostykerei.news.domain.Feed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.scheduling.annotation.Async;

public class Crawler {

    @Autowired
    private FeedDao feedDao;

    @Autowired
    private FeedCrawler feedCrawler;

    @Async
    public void run() {

        Feed feed = feedDao.pollFeedToProcess();

        if (feed != null) {
            feedCrawler.crawlFeed(feed);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        System.setProperty("spring.profiles.active", "create-db,fill-masterdata,fill-db,test");
        //System.setProperty("spring.profiles.active", "test");
        ApplicationContext context =
                new ClassPathXmlApplicationContext("crawlerContext.xml");

        Crawler c = (Crawler) context.getBean("crawler");

        c.run();
        System.exit(0);
    }
}
