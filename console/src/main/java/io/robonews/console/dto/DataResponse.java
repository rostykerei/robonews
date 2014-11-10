/**
 * Robonews.io
 *
 * Copyright (c) 2013-2014 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.console.dto;

import java.io.PrintWriter;
import java.io.StringWriter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonSerialize;

public class DataResponse<T> {

    private T data;

    private boolean error = false;

    private Exception exception;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
    public T getData() {
        return data;
    }

    public void setData(T response) {
        this.data = response;
    }

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
}
