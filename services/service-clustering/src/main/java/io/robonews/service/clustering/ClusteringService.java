/**
 * Robonews.io
 *
 * Copyright (c) 2013-2015 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.service.clustering;

import java.util.List;
import java.util.Set;

/**
 * Created by rosty on 04/08/15.
 */
public interface ClusteringService {

    List<Cluster> process(Set<Document> documents, int desiredClusterCount);

}
