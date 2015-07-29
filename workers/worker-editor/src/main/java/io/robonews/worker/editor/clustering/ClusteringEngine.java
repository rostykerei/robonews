/**
 * Robonews.io
 *
 * Copyright (c) 2013-2015 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.worker.editor.clustering;

import io.robonews.domain.Story;
import io.robonews.worker.editor.clustering.impl.Cluster;

import java.util.List;

/**
 * Created by rosty on 25/07/15.
 */
public interface ClusteringEngine {

    List<Cluster> clusterize(List<Story> selection);

}
