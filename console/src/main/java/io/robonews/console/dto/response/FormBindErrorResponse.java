/**
 * Robonews.io
 *
 * Copyright (c) 2013-2014 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.console.dto.response;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class FormBindErrorResponse {

    public final boolean error = true;

    private Map<String, Set<String>> errors = new HashMap<>();

    public void addError(String field, String err) {

        if (!errors.containsKey(field)) {
            errors.put(field, new HashSet<String>());
        }

        errors.get(field).add(err);
    }

    public Map<String, Set<String>> getErrors() {
        return errors;
    }

}
