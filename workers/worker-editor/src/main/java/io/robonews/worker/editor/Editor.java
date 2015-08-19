/**
 * Robonews.io
 *
 * Copyright (c) 2013-2015 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.worker.editor;

import io.robonews.dao.SectionDao;
import io.robonews.dao.StoryDao;
import io.robonews.domain.Feed;
import io.robonews.domain.Section;
import io.robonews.domain.Story;
import io.robonews.messaging.domain.SectionComposeMessage;
import io.robonews.service.clustering.Cluster;
import io.robonews.service.clustering.ClusteringService;
import io.robonews.service.clustering.Document;
import io.robonews.service.clustering.DocumentComparator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.util.stream.Collectors;

public class Editor {

    @Autowired
    private SectionDao sectionDao;

    @Autowired
    private StoryDao storyDao;

    @Autowired
    private ClusteringService clusteringService;

    private Logger logger = LoggerFactory.getLogger(Editor.class);

    public void listen(SectionComposeMessage message) {
        Section section = sectionDao.getById(message.getSectionId());

        if (section != null) {
            if (message.getTimestampFrom() == null) {
                logger.error("Received SectionComposeMessage with null timestamp-from");
                return;
            }

            if (message.getTimestampTo() == null) {
                logger.error("Received SectionComposeMessage with null timestamp-to");
                return;
            }

            composeSection(section, message.getTimestampFrom(), message.getTimestampTo());
        }
        else {
            logger.error("Received SectionComposeMessage with invalid section-id: " + message.getSectionId());
        }
    }

    private void composeSection(Section section, Date timeFrom, Date timeTo) {

        Set<Feed> leadFeeds = section.getLeadFeeds();

        Set<Document> topStories = new TreeSet<>(DocumentComparator.ID_DESC);

        for (Feed leadFeed : leadFeeds) {

            List<Story> stories = storyDao.getFeedStories(leadFeed.getId(), timeFrom, timeTo, 1000);

            Set<Document> documents = stories
                    .parallelStream()
                    .map(story -> new Document(
                            Long.toString(story.getId()),
                            story.getTitle(),
                            story.getDescription()))
                    .collect(Collectors.toSet());

            List<Cluster> clusters = clusteringService.process(documents, 20);

            for (Cluster cluster : clusters) {
                if (cluster.getScore() < 1) {
                    continue;
                }

                double score = cluster.getDocuments().size() * 100d / stories.size();

                for (Document d : cluster.getDocuments()) {
                    if (!topStories.contains(d)) {
                        d.setScore(score);
                        topStories.add(d);
                        break;
                    }
                }
            }
        }

        List<Cluster> topClusters = clusteringService.process(topStories, 20);

        for (Cluster cluster : topClusters) {
            System.out.println(cluster.getName() + " (" + cluster.getDocuments().size() + " docs, score: " + cluster.getScore() + ")");

            Set<Document> set = new TreeSet<>(DocumentComparator.ID_DESC);
            set.addAll(cluster.getDocuments());

            for (Document document : set) {
                System.out.println("\t[" + document.getId() + "] " + document.getTitle());
            }
        }


    }
}
