/**
 * Robonews.io
 *
 * Copyright (c) 2013-2015 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.console.controller.advice;

import io.robonews.console.controller.error.FormBindException;
import io.robonews.console.dto.response.FormBindErrorResponse;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class FormBindExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(FormBindException.class)
    public @ResponseBody FormBindErrorResponse handleInvalidRequest(FormBindException e) {

        FormBindErrorResponse result = new FormBindErrorResponse();

        for (FieldError fieldError : e.getErrors().getFieldErrors()) {
            result.addError(fieldError.getField(), fieldError.getDefaultMessage());
        }

        return result;
    }
}
