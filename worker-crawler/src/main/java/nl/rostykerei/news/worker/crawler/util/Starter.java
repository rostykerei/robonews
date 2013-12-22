package nl.rostykerei.news.worker.crawler.util;

import nl.rostykerei.news.worker.crawler.controller.CrawlerController;

/**
 * Created with IntelliJ IDEA.
 * User: rosty
 * Date: 12/21/13
 * Time: 8:47 PM
 * To change this template use File | Settings | File Templates.
 */
public class Starter {

    public Starter(CrawlerController crawlerController) {
        while (true) {
            crawlerController.execute();
        }
    }

}
