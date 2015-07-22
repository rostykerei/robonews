/**
 * Robonews.io
 *
 * Copyright (c) 2013-2015 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.worker.solr.agent;

import io.robonews.solr.domain.StoryDocument;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.junit.Test;

public class SolrQueueListenerTest {

    private static HttpSolrClient solrCore = new HttpSolrClient( "http://localhost:8983/solr/robonews");

    @Test
    public void test() throws Exception {
        StoryDocument storyDocument = new StoryDocument();
        storyDocument.setId(777);
        storyDocument.setUid("xyz");
        storyDocument.setTitle("Test-title");
        storyDocument.setTags(new String[]{"tag1", "tag2", "tag3"});

        //solrCore.addBean(storyDocument);
        //solrCore.commit();
    }
}