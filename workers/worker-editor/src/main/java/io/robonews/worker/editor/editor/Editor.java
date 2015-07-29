/**
 * Robonews.io
 *
 * Copyright (c) 2013-2015 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.worker.editor.editor;

import io.robonews.worker.editor.clustering.impl.Cluster;
import io.robonews.worker.editor.editor.impl.EditorResult;

import java.util.List;

/**
 * Created by rosty on 25/07/15.
 */
public interface Editor {

    EditorResult makeUp(List<Cluster> clusters);

}
