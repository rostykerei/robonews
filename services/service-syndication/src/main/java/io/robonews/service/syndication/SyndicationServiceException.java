/**
 * Robonews.io
 *
 * Copyright (c) 2013-2014 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.service.syndication;

import java.io.IOException;

public class SyndicationServiceException extends IOException {

    public SyndicationServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
