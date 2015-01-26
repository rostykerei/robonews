/**
 * Robonews.io
 *
 * Copyright (c) 2013-2015 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.console.controller.error;

import org.springframework.validation.Errors;

public class FormBindException extends RuntimeException {

    private Errors errors;

    public FormBindException(Errors errors) {
        this.errors = errors;
    }

    public Errors getErrors() {
        return errors;
    }
}
