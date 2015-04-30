/**
 * Robonews.io
 *
 * Copyright (c) 2013-2015 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.service.alexa.impl;

public class AlexaServiceResult {

    private int rank;

    private String siteBase;

    private String title;

    private String description;

    private String countryCode;

    public AlexaServiceResult(int rank, String siteBase, String title, String description, String countryCode) {
        this.rank = rank;
        this.siteBase = siteBase;
        this.title = title;
        this.description = description;
        this.countryCode = countryCode;
    }

    public int getRank() {
        return rank;
    }

    public String getSiteBase() {
        return siteBase;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getCountryCode() {
        return countryCode;
    }

}
