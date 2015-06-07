/**
 * Robonews.io
 *
 * Copyright (c) 2013-2015 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.worker.crawler.util;

import io.robonews.worker.crawler.controller.CrawlerController;

public class Starter {

    public Starter(CrawlerController crawlerController) {
        while (true) {
            crawlerController.execute();

            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
