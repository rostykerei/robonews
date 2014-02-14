/**
 * Robonews.io
 *
 * Copyright (c) 2013-2014 Rosty Kerei.
 * All rights reserved.
 */
package nl.rostykerei.news.service.nlp.stanford;

import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.util.Triple;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import nl.rostykerei.news.service.nlp.NamedEntityRecognizerService;
import nl.rostykerei.news.service.nlp.impl.NamedEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NamedEntityRecognizerServiceStanford implements NamedEntityRecognizerService {

    private AbstractSequenceClassifier<CoreLabel> classifier;

    private Logger logger = LoggerFactory.getLogger(NamedEntityRecognizerServiceStanford.class);

    public NamedEntityRecognizerServiceStanford(String modelFile) {
        classifier = CRFClassifier.getClassifierNoExceptions(modelFile);
    }

    @Override
    public Set<NamedEntity> getNamedEntities(String text) {
        Set<NamedEntity> result = new HashSet<NamedEntity>();

        List<Triple<String,Integer,Integer>> neTriples = classifier.classifyToCharacterOffsets(text);

        if (neTriples != null && neTriples.size() > 0) {
            for (Triple<String,Integer,Integer> neTriple : neTriples) {
                try {
                    NamedEntity.Type neType = NamedEntity.Type.valueOf(neTriple.first());
                    String neName= text.substring(neTriple.second(), neTriple.third());

                    result.add(new NamedEntity(neType, neName));
                }
                catch (IllegalArgumentException e) {
                    logger.warn("Unknown named entity type: " + neTriple.first());
                }
            }
        }

        return result;
    }
}
