/**
 * Robonews.io
 *
 * Copyright (c) 2013-2014 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.service.facebook;

import io.robonews.service.facebook.model.Page;

import java.util.List;

public interface FacebookService {

    List<Page> searchPage(String query);

    List<Page> searchPage(String query, int limit);

}
