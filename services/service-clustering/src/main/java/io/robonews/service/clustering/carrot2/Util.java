/**
 * Robonews.io
 *
 * Copyright (c) 2013-2015 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.service.clustering.carrot2;

import io.robonews.service.clustering.Cluster;
import io.robonews.service.clustering.Document;
import org.carrot2.core.LanguageCode;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by rosty on 04/08/15.
 */
public class Util {

    public static List<Cluster> convertClusters(List<org.carrot2.core.Cluster> carrotClusters) {

        List<Cluster> result = new ArrayList<>();

        for (org.carrot2.core.Cluster carrotCluster : carrotClusters) {
            Cluster cluster = new Cluster();

            cluster.setName(carrotCluster.getLabel());
            cluster.setScore( carrotCluster.getScore() );

            for (org.carrot2.core.Document carrotDocument : carrotCluster.getDocuments() ) {
                Document document = new Document(
                        carrotDocument.getStringId(),
                        carrotDocument.getTitle(),
                        carrotDocument.getSummary(),
                        carrotDocument.getScore()
                );

                cluster.getDocuments().add(document);
            }

            result.add(cluster);
        }

        return result;
    }

    public static List<org.carrot2.core.Document> convertDocuments(Set<Document> documents) {
        List<org.carrot2.core.Document> result = new ArrayList<>();

        for (Document document : documents) {
            org.carrot2.core.Document carrotDoc =
                    new org.carrot2.core.Document(document.getTitle(),
                            document.getContent(), null, LanguageCode.ENGLISH,
                            document.getId());

            if (document.getScore() != null) {
                carrotDoc.setScore(document.getScore());
            }

            result.add(carrotDoc);
        }

        return result;
    }
}
