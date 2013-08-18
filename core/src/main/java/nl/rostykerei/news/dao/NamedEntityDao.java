package nl.rostykerei.news.dao;

import nl.rostykerei.news.domain.NamedEntity;
import nl.rostykerei.news.domain.NamedEntityType;

public interface NamedEntityDao extends AbstractDao<NamedEntity, Integer> {

    NamedEntity findOrCreateNamedEntity(String name, NamedEntityType type);
}
