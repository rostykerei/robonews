package nl.rostykerei.news.worker.crawler.util;

import nl.rostykerei.news.worker.crawler.controller.CrawlerController;

public class Starter {

    public Starter(CrawlerController crawlerController) {
        while (true) {
            crawlerController.execute();
        }
    }

}