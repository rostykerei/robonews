package nl.rostykerei.news.service.freebase;

import nl.rostykerei.news.service.freebase.exception.FreebaseServiceException;
import nl.rostykerei.news.service.freebase.impl.FreebaseSearchResult;

public interface FreebaseService {

    FreebaseSearchResult searchPerson(String query) throws FreebaseServiceException;

    FreebaseSearchResult searchLocation(String query) throws FreebaseServiceException;

    FreebaseSearchResult searchOrganization(String query) throws FreebaseServiceException;

    FreebaseSearchResult searchMisc(String query) throws FreebaseServiceException;

}
