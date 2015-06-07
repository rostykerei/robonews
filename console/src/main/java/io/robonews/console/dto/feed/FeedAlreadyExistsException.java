/**
 * Robonews.io
 *
 * Copyright (c) 2013-2015 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.console.dto.feed;

/**
 * Created by rosty on 06/06/15.
 */
public class FeedAlreadyExistsException extends RuntimeException {

    public FeedAlreadyExistsException(String message) {
        super(message);
    }
}
