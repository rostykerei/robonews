/**
 * Robonews.io
 *
 * Copyright (c) 2013-2015 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.console.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.PrintWriter;
import java.io.StringWriter;

public class BaseResponse {

    private boolean error = false;

    private Exception exception;

    @JsonIgnore
    public Exception getException() {
        return exception;
    }

    @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
    public String getStackTrace() {
        if (exception != null) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw, true);
            exception.printStackTrace(pw);
            return sw.getBuffer().toString();
        }

        return null;
    }

    @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
    public String getExceptionName() {
        if (exception != null) {
            return exception.getClass().getName();
        }

        return null;
    }

    @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
    public String getExceptionMessage() {
        if (exception != null) {
            return exception.getMessage();
        }

        return null;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }
}
