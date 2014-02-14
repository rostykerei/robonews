/**
 * Robonews.io
 *
 * Copyright (c) 2013-2014 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.service.freebase.exception;


public class ServiceUnavailableException extends FreebaseServiceException {

    public ServiceUnavailableException() {
        super();
    }

    public ServiceUnavailableException(String error) {
        super(error);
    }
}
