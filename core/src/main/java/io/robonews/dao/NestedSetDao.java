/**
 * Robonews.io
 *
 * Copyright (c) 2013-2015 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.dao;

import io.robonews.domain.NestedNode;

import java.util.List;

/**
 * Created by rosty on 04/05/15.
 */
public interface NestedSetDao<T extends NestedNode> {

    T getById(int id);

    T createAsRoot(T rootCategory);

    int create(T category, T parentCategory);

    int create(T Category, int parentCategoryId);

    List<T> getAll();

    T getParent(T category);

    List<T> getChildren(T category);

    List<T> getChildren(T category, int depth);

    boolean hasChildren(T category);

    boolean hasParent(T category);

    void delete(T category);
}
