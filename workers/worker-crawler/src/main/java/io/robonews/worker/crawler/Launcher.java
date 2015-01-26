/**
 * Robonews.io
 *
 * Copyright (c) 2013-2015 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.worker.crawler;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Launcher {

    public static void main(String... args) {
        new ClassPathXmlApplicationContext("workerCrawlerContext.xml");
    }
}
