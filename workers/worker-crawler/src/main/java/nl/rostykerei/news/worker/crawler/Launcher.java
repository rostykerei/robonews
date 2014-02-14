/**
 * Robonews.io
 *
 * Copyright (c) 2013-2014 Rosty Kerei.
 * All rights reserved.
 */
package nl.rostykerei.news.worker.crawler;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Launcher {

    public static void main(String... args) {
        new ClassPathXmlApplicationContext("workerCrawlerContext.xml");
    }
}
