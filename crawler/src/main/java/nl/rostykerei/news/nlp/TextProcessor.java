package nl.rostykerei.news.nlp;

import nl.rostykerei.news.domain.NamedEntity;

import java.util.Set;

public interface TextProcessor {

    Set<TextNamedEntity> getNamedEntities(String text);

}
