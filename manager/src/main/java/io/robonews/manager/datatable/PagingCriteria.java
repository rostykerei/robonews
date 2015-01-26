/**
 * Robonews.io
 *
 * Copyright (c) 2013-2015 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.manager.datatable;

import java.util.Collections;
import java.util.List;

public final class PagingCriteria
{
    private final Integer displayStart;
    private final Integer displaySize;
    private final List<SortField> sortFields;
    private final Integer pageNumber;

    public PagingCriteria(Integer displayStart, Integer displaySize, Integer pageNumber, List<SortField> sortFields)
    {
        this.displayStart = displayStart;
        this.displaySize = displaySize;
        this.pageNumber = pageNumber;
        this.sortFields = sortFields;
    }

    public Integer getDisplayStart() {
        return displayStart;
    }

    public Integer getDisplaySize() {
        return displaySize;
    }

    public List<SortField> getSortFields()
    {
        return Collections.unmodifiableList(sortFields);
    }

    public Integer getPageNumber()
    {
        return pageNumber;
    }

}