package nl.rostykerei.news.dao;

import nl.rostykerei.news.domain.Tag;
import nl.rostykerei.news.domain.TagAlternative;
import nl.rostykerei.news.domain.TagAmbiguous;

public interface TagDao extends AbstractDao<Tag, Integer> {

    Tag findByFreebaseMid(String freebaseMid);

    Tag findByAlternative(String altName);

    void logAmbiguous(String ambiguousName);

    TagAmbiguous findTagAmbiguous(String ambiguousName);
}
