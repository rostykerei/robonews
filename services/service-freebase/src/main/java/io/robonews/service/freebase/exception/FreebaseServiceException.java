/**
 * Robonews.io
 *
 * Copyright (c) 2013-2015 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.service.freebase.exception;


public class FreebaseServiceException extends Exception {

    public FreebaseServiceException() {
        super();
    }

    public FreebaseServiceException(String error) {
        super(error);
    }
}
