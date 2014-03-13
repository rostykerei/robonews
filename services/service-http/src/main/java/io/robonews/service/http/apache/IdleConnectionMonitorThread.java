/**
 * Robonews.io
 *
 * Copyright (c) 2013-2014 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.service.http.apache;

import java.util.concurrent.TimeUnit;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IdleConnectionMonitorThread extends Thread {

    private final PoolingHttpClientConnectionManager connectionManager;
    private volatile boolean shutdown = false;

    private static final long FIXED_TIMEOUT = 5000L; // Ticks every 5 sec
    private static final long IDLE_TIMEOUT = 30L; // Idle connections lasts for max 30sec

    private Logger logger = LoggerFactory.getLogger(IdleConnectionMonitorThread.class);

    public IdleConnectionMonitorThread(PoolingHttpClientConnectionManager connMgr) {
        super();
        this.connectionManager = connMgr;
    }

    @Override
    public void run() {
        try {
            while (!shutdown) {
                synchronized (this) {
                    wait(FIXED_TIMEOUT);

                    if (connectionManager != null) {

                        // Close expired connections
                        connectionManager.closeExpiredConnections();

                        // Optionally, close connections
                        // that have been idle longer than 30 sec
                        connectionManager.closeIdleConnections(IDLE_TIMEOUT, TimeUnit.SECONDS);

                        if (logger.isDebugEnabled()) {
                            logger.debug("HTTP Connection manager stats: " + connectionManager.getTotalStats().toString());
                        }
                    }
                    else {
                        shutdown();
                    }
                }
            }
        } catch (InterruptedException ex) {
            // terminate
        }
    }

    public void shutdown() {
        shutdown = true;
        synchronized (this) {
            notifyAll();
        }
    }

}
