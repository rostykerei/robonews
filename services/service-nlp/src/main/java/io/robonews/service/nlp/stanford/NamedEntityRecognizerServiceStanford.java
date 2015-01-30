/**
 * Robonews.io
 *
 * Copyright (c) 2013-2015 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.service.nlp.stanford;

import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.util.Pair;
import edu.stanford.nlp.util.Triple;
import io.robonews.service.nlp.NamedEntityRecognizerService;
import io.robonews.service.nlp.impl.NamedEntity;
import io.robonews.service.nlp.util.WordCounter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class NamedEntityRecognizerServiceStanford implements NamedEntityRecognizerService {

    private AbstractSequenceClassifier<CoreLabel> classifier;

    private Logger logger = LoggerFactory.getLogger(NamedEntityRecognizerServiceStanford.class);

    public NamedEntityRecognizerServiceStanford(String modelFile) {
        classifier = CRFClassifier.getClassifierNoExceptions(modelFile);
    }

    @Override
    public Set<NamedEntity> getNamedEntities(String text) {
        Set<NamedEntity> result = new HashSet<>();

        Map<Pair<NamedEntity.Type, String>, Integer> neMap = new HashMap<>();

        List<Triple<String,Integer,Integer>> neTriples = classifier.classifyToCharacterOffsets(text);

        if (neTriples != null && neTriples.size() > 0) {

            int wordsTotal = WordCounter.count(text);

            for (Triple<String,Integer,Integer> neTriple : neTriples) {
                NamedEntity.Type neType;

                try {
                    neType = NamedEntity.Type.valueOf(neTriple.first());
                }
                catch (RuntimeException e) {
                    logger.error("Unknown named entity type: " + neTriple.first());
                    continue;
                }

                String neName= text.substring(neTriple.second(), neTriple.third());

                wordsTotal = wordsTotal - WordCounter.count(neName) + 1;

                Pair<NamedEntity.Type, String> entity = new Pair<>(neType, neName);

                Integer existent = neMap.remove(entity);

                if (existent == null) {
                    neMap.put(entity, 1);
                }
                else {
                    neMap.put(entity, existent + 1);
                }
            }

            for (Map.Entry<Pair<NamedEntity.Type, String>, Integer> entry : neMap.entrySet()) {
                result.add(
                    new NamedEntity(
                        entry.getKey().first(),
                        entry.getKey().second(),
                        (float) entry.getValue() / wordsTotal  // TF calculation is here
                    )
                );
            }
        }

        return result;
    }


}
