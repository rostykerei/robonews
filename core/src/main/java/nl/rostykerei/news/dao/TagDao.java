package nl.rostykerei.news.dao;

import nl.rostykerei.news.domain.Tag;

public interface TagDao extends AbstractDao<Tag, Integer> {

    Tag findOrCreateNamedEntity(String name, Tag.Type type);
}
