/**
 * Robonews.io
 *
 * Copyright (c) 2013-2014 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.console.dto.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class DataResponse<T> extends BaseResponse {

    private T data;

    @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
    public T getData() {
        return data;
    }

    public void setData(T response) {
        this.data = response;
    }

}
