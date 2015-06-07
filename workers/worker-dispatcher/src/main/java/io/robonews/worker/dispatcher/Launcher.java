/**
 * Robonews.io
 *
 * Copyright (c) 2013-2015 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.worker.dispatcher;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Launcher {
    public static void main(String... args) {
        new ClassPathXmlApplicationContext("workerDispatcherContext.xml");
    }
}
