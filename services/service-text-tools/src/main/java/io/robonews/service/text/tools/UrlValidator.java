/**
 * Robonews.io
 *
 * Copyright (c) 2013-2014 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.service.text.tools;

/**
 * URL Validator
 *
 * @author Rosty Kerei
 */
public class UrlValidator {

    private static final org.apache.commons.validator.routines.UrlValidator urlValidator =
                            new org.apache.commons.validator.routines.UrlValidator(new String[] {"http"});

    /**
     * Checks if input string is a valid URL
     * Note: it only checks agains HTTP schemaa
     *
     * @param url URL to check
     * @return true if valid, false if not
     */
    public static boolean isValid(String url) {
        return urlValidator.isValid(url);
    }
}
