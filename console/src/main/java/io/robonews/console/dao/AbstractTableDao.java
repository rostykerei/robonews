/**
 * Robonews.io
 *
 * Copyright (c) 2013-2014 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.console.dao;

import io.robonews.console.datatable.Datatable;
import io.robonews.console.datatable.DatatableCriteria;
import io.robonews.dao.AbstractDao;
import java.io.Serializable;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

public abstract class AbstractTableDao<T, E> {

    protected abstract AbstractDao<T, ? extends Serializable> getDao();

    protected abstract String[] getSearchFields();

    protected abstract E mapFromOriginal(T obj);

    @Transactional(readOnly = true)
    public Datatable<E> getDatatable(DatatableCriteria criteria) {

        Datatable<E> datatable = new Datatable<>();

        datatable.setDraw(criteria.getDraw());
        datatable.setRecordsTotal(getDao().getCountAll());
        datatable.setRecordsFiltered(getDao().getTableCount(criteria.getSearch(), getSearchFields()));

        List<T> list = getDao().getTable(
            criteria.getStart(),
            criteria.getLength(),
            criteria.getSortField(),
            criteria.getSortDirection() == DatatableCriteria.SortDirection.ASC,
            criteria.getSearch(),
            getSearchFields()
        );

        for (T channel : list) {
            datatable.getData().add(mapFromOriginal(channel));
        }

        return datatable;
    }

}
