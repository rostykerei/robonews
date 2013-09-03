package nl.rostykerei.news.dao;

import nl.rostykerei.news.domain.Tag;
import nl.rostykerei.news.domain.TagAlternative;

public interface TagDao extends AbstractDao<Tag, Integer> {

    Tag createTagWithAlternative(String tagName, Tag.Type tagType, String freebaseMid, boolean isAmbiguous,
                                 String altName, Tag.Type altType, float altConfidence);

    TagAlternative createTagAlternative(Tag tag, Tag.Type altType, String altName, float altConfidence);

    Tag findByFreebaseMind(String freebaseMid);

    Tag findByAlternative(String altName, Tag.Type type);

}
