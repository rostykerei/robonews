package nl.rostykerei.news.dao;

import nl.rostykerei.news.domain.Tag;
import nl.rostykerei.news.domain.TagAlternative;
import nl.rostykerei.news.domain.TagAmbiguous;

public interface TagDao extends AbstractDao<Tag, Integer> {

    Tag findOrCreateTagWithAlternative(String tagName, String alternativeName,
                                       float alternativeConfidence,
                                       String freebaseMid, Tag.Type type);

    Tag findByAlternative(String altName);

    // TagAmbiguous

    TagAmbiguous findTagAmbiguous(String ambiguousName);

    void createTagAmbiguous(String ambiguousName);

    void saveTagAmbiguous(TagAmbiguous tagAmbiguous);
}
