/**
 * Robonews.io
 *
 * Copyright (c) 2013-2015 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.worker.solr.agent;

import io.robonews.dao.StoryDao;
import io.robonews.domain.Story;
import io.robonews.domain.Tag;
import io.robonews.messaging.domain.SolrCreateMessage;
import io.robonews.solr.domain.StoryDocument;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;

public class SolrQueueListener {

    @Autowired
    private StoryDao storyDao;

    @Autowired
    private SolrClient solrClient;

    @Value("${solr.commitWithin}")
    private int commitWithin;

    private Logger logger = LoggerFactory.getLogger(SolrQueueListener.class);

    public void listen(SolrCreateMessage message) {
        try {
            processMessage(message);
        }
        catch (Exception e) {
            logger.info("Failed to process message: ", e);
        }
    }

    private void processMessage(SolrCreateMessage message) throws IOException, SolrServerException {
        Story story = storyDao.getByIdWithTags(message.getStoryId());

        if (story == null) {
            logger.info("Story not found");
            return;
        }

        StoryDocument storyDocument = new StoryDocument();

        storyDocument.setId(story.getId());
        storyDocument.setUid(story.getUid());
        storyDocument.setTitle(story.getTitle());
        storyDocument.setAuthor(story.getAuthor());
        storyDocument.setLink(story.getLink());
        storyDocument.setVideo(story.isVideo());
        storyDocument.setPublicationDate(story.getPublicationDate());
        storyDocument.setAdjustedPublicationDate(story.getAdjustedPublicationDate());
        storyDocument.setCreatedDate(story.getCreatedDate());
        storyDocument.setText(story.getDescription());
        storyDocument.setChannel(story.getChannel().getCanonicalName());
        storyDocument.setOriginalFeedId(story.getOriginalFeed().getId());

        storyDocument.setAreaId(story.getArea().getId());
        storyDocument.setTopicId(story.getTopic().getId());

        storyDocument.setTags(
                story
                        .getTags()
                        .stream()
                        .map(Tag::getName)
                        .toArray(String[]::new)
        );

        solrClient.addBean(storyDocument, commitWithin);
    }

}
