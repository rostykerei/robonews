/**
 * Robonews.io
 *
 * Copyright (c) 2013-2014 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.dao;

import java.io.Serializable;
import java.util.List;

public interface AbstractDao <T, PK extends Serializable> {

    T getById(PK id);

    long getTableCount(String search, String[] searchFields);

    List<T> getTable(int start, int length, String sortField, boolean isSortAscending, String search, String[] searchFields);

    PK create(T newInstance);

    void createOrUpdate(T transientObject);

    void update(T transientObject);

    void delete(T persistentObject);

    long getCountAll();
}
