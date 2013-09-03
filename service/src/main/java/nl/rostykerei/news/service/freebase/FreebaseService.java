package nl.rostykerei.news.service.freebase;

import nl.rostykerei.news.service.freebase.exception.FreebaseServiceException;
import nl.rostykerei.news.service.freebase.impl.FreebaseSearchResult;
import nl.rostykerei.news.service.nlp.impl.NamedEntity;

public interface FreebaseService {

    FreebaseSearchResult search(NamedEntity namedEntity) throws FreebaseServiceException;

}
