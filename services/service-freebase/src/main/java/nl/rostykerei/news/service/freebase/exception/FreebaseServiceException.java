/**
 * Robonews.io
 *
 * Copyright (c) 2013-2014 Rosty Kerei.
 * All rights reserved.
 */
package nl.rostykerei.news.service.freebase.exception;


public class FreebaseServiceException extends Exception {

    public FreebaseServiceException() {
        super();
    }

    public FreebaseServiceException(String error) {
        super(error);
    }
}
