/**
 * Robonews.io
 *
 * Copyright (c) 2013-2015 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.service.clustering.carrot2;

import io.robonews.service.clustering.Cluster;
import io.robonews.service.clustering.ClusteringService;
import io.robonews.service.clustering.Document;
import org.carrot2.clustering.lingo.ClusterBuilderDescriptor;
import org.carrot2.clustering.lingo.LingoClusteringAlgorithm;
import org.carrot2.clustering.lingo.LingoClusteringAlgorithmDescriptor;
import org.carrot2.core.Controller;
import org.carrot2.core.ControllerFactory;
import org.carrot2.core.ProcessingResult;
import org.carrot2.text.preprocessing.filter.NumericLabelFilterDescriptor;
import org.carrot2.text.vsm.TermDocumentMatrixBuilderDescriptor;

import java.util.*;

public class ClusteringServiceLingo implements ClusteringService {

    private double scoreWeight = 0.5;
    private double clusterMergingThreshold = 0.35;
    private double phraseLabelBoost = 3.5;
    private double titleWordsBoost = 9.0;
    private boolean enableNumericLabelFilter = false;

    @Override
    public List<Cluster> process(Set<Document> documents, int desiredClusterCount) {

        final Map<String, Object> attributes = new HashMap<String, Object>();

        LingoClusteringAlgorithmDescriptor
                .attributeBuilder(attributes)
                .documents(Util.convertDocuments(documents))
                .desiredClusterCountBase(desiredClusterCount)
                .scoreWeight(getScoreWeight());

        ClusterBuilderDescriptor
                .attributeBuilder(attributes)
                .clusterMergingThreshold(getClusterMergingThreshold())
                .phraseLabelBoost(getPhraseLabelBoost());

        TermDocumentMatrixBuilderDescriptor
                .attributeBuilder(attributes)
                .titleWordsBoost(getTitleWordsBoost());

        NumericLabelFilterDescriptor
                .attributeBuilder(attributes)
                .enabled(isEnableNumericLabelFilter());

        Controller controller = ControllerFactory.createSimple();

        final ProcessingResult result = controller.process(attributes, LingoClusteringAlgorithm.class);
        final List<org.carrot2.core.Cluster> clusters = result.getClusters();

        return Util.convertClusters(clusters);
    }

    public double getScoreWeight() {
        return scoreWeight;
    }

    public void setScoreWeight(double scoreWeight) {
        this.scoreWeight = scoreWeight;
    }

    public double getClusterMergingThreshold() {
        return clusterMergingThreshold;
    }

    public void setClusterMergingThreshold(double clusterMergingThreshold) {
        this.clusterMergingThreshold = clusterMergingThreshold;
    }

    public double getPhraseLabelBoost() {
        return phraseLabelBoost;
    }

    public void setPhraseLabelBoost(double phraseLabelBoost) {
        this.phraseLabelBoost = phraseLabelBoost;
    }

    public double getTitleWordsBoost() {
        return titleWordsBoost;
    }

    public void setTitleWordsBoost(double titleWordsBoost) {
        this.titleWordsBoost = titleWordsBoost;
    }

    public boolean isEnableNumericLabelFilter() {
        return enableNumericLabelFilter;
    }

    public void setEnableNumericLabelFilter(boolean enableNumericLabelFilter) {
        this.enableNumericLabelFilter = enableNumericLabelFilter;
    }
}
