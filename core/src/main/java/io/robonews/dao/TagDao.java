/**
 * Robonews.io
 *
 * Copyright (c) 2013-2015 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.dao;

import io.robonews.domain.Tag;
import io.robonews.domain.TagAlternative;

public interface TagDao extends AbstractDao<Tag, Integer> {

    Tag createTagWithAlternative(String tagName, Tag.Type tagType, String freebaseMid, boolean isAmbiguous,
                                 String altName, Tag.Type altType, float altConfidence);

    TagAlternative createTagAlternative(Tag tag, Tag.Type altType, String altName, float altConfidence);

    Tag findByFreebaseMid(String freebaseMid);

    Tag findByAlternative(String altName, Tag.Type type);

}
