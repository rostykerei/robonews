/**
 * Robonews.io
 *
 * Copyright (c) 2013-2014 Rosty Kerei.
 * All rights reserved.
 */
package nl.rostykerei.news.manager.datatable;

public enum SortDirection
{
    ASC("asc"), DESC("desc");

    private String direction;

    private SortDirection(String direction)
    {
        this.direction = direction;
    }

    public String getDirection()
    {
        return this.direction;
    }

    public static SortDirection valueOfCaseInsensitive(String value)
    {
        String valueUpper = value.toUpperCase();
        SortDirection sd = SortDirection.valueOf(valueUpper);

        return sd;
    }
}