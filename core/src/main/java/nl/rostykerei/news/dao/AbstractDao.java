package nl.rostykerei.news.dao;

import java.io.Serializable;

public interface AbstractDao <T, PK extends Serializable> {

    T getById(PK id);

    PK create(T newInstance);

    void createOrUpdate(T transientObject);

    void update(T transientObject);

    void delete(T persistentObject);

    int getCountAll();
}
