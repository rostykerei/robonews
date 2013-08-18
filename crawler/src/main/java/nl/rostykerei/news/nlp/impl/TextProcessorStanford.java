package nl.rostykerei.news.nlp.impl;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations;
import edu.stanford.nlp.util.CoreMap;
import nl.rostykerei.news.dao.NamedEntityDao;
import nl.rostykerei.news.domain.NamedEntity;
import nl.rostykerei.news.domain.NamedEntityType;
import nl.rostykerei.news.nlp.TextNamedEntity;
import nl.rostykerei.news.nlp.TextProcessor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TextProcessorStanford implements TextProcessor {

    private StanfordCoreNLP pipeline;

    public TextProcessorStanford(StanfordCoreNLP pipeline) {
        this.pipeline = pipeline;
    }

    @Override
    public Set<TextNamedEntity> getNamedEntities(String text) {
        Set<TextNamedEntity> result = new HashSet<TextNamedEntity>();

        Annotation document = new Annotation(text);
        pipeline.annotate(document);

        List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);

        int wordNumber = 0;
        int lastWordNumber = Integer.MIN_VALUE;

        NamedEntityType lastType = null;
        TextNamedEntity lastNamedEntity = null;

        for(CoreMap sentence: sentences) {
            for (CoreLabel token: sentence.get(CoreAnnotations.TokensAnnotation.class)) {

                String word = token.get(CoreAnnotations.TextAnnotation.class);
                String ne = token.get(CoreAnnotations.NamedEntityTagAnnotation.class);

                if (ne != null  && !ne.equals("O")) {
                    NamedEntityType type = null;

                    if (ne.equals("PERSON")) {
                        type = NamedEntityType.PERSON;
                    }
                    else if (ne.equals("LOCATION")) {
                        type = NamedEntityType.LOCATION;
                    }
                    else if (ne.equals("ORGANIZATION")) {
                        type = NamedEntityType.ORGANIZATION;
                    }
                    else if (ne.equals("MISC")) {
                        type = NamedEntityType.MISC;
                    }

                    if (type != null) {
                        if (type == lastType && lastWordNumber == (wordNumber - 1)) {
                            result.remove(lastNamedEntity);
                            lastNamedEntity.setName(lastNamedEntity.getName() + " " + word);
                            result.add(lastNamedEntity);
                        }
                        else {
                            lastNamedEntity = new TextNamedEntity(word, type);
                            lastType = type;
                            result.add(lastNamedEntity);
                        }

                        lastWordNumber = wordNumber;
                    }
                }

                wordNumber++;
            }
        }

        return result;
    }
}
