package nl.rostykerei.news.crawler;

import nl.rostykerei.news.crawler.controller.CrawlerController;
import nl.rostykerei.news.dao.FeedDao;
import nl.rostykerei.news.domain.Feed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class CrawlerRunner {
    public static void main(String[] args) throws InterruptedException {
        System.setProperty("spring.profiles.active", "create-db,fill-masterdata,fill-db,test");
        //System.setProperty("spring.profiles.active", "test");
        ApplicationContext context =
                new ClassPathXmlApplicationContext("crawlerContext.xml");

        CrawlerController c = (CrawlerController) context.getBean("crawler");

        c.execute();
        System.exit(0);
    }
}
