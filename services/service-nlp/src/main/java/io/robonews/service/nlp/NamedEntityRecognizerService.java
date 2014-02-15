/**
 * Robonews.io
 *
 * Copyright (c) 2013-2014 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.service.nlp;

import io.robonews.service.nlp.impl.NamedEntity;

import java.util.Set;

public interface NamedEntityRecognizerService {

    Set<NamedEntity> getNamedEntities(String text);

}
