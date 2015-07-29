/**
 * Robonews.io
 *
 * Copyright (c) 2013-2015 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.worker.editor.section;

import io.robonews.domain.Story;

import java.util.List;

/**
 * Created by rosty on 25/07/15.
 */
public interface Section {

    String getName();

    List<Story> getSelection();

}
