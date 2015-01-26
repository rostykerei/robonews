/**
 * Robonews.io
 *
 * Copyright (c) 2013-2015 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.service.text.tools;

public class DomainValidator {

    public static boolean isValid(String domain) {
        return org.apache.commons.validator.routines.DomainValidator.getInstance().isValid(domain);
    }
}
