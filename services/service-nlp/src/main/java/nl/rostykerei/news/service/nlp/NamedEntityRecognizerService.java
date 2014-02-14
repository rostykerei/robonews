/**
 * Robonews.io
 *
 * Copyright (c) 2013-2014 Rosty Kerei.
 * All rights reserved.
 */
package nl.rostykerei.news.service.nlp;

import java.util.Set;
import nl.rostykerei.news.service.nlp.impl.NamedEntity;

public interface NamedEntityRecognizerService {

    Set<NamedEntity> getNamedEntities(String text);

}
