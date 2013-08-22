package nl.rostykerei.news.service.freebase;

import nl.rostykerei.news.service.freebase.exception.FreebaseServiceException;
import nl.rostykerei.news.service.freebase.impl.FreebaseSearchResult;

public interface FreebaseService {

    FreebaseSearchResult searchForPerson(String person) throws FreebaseServiceException;

    FreebaseSearchResult searchForLocation(String location) throws FreebaseServiceException;

    FreebaseSearchResult searchForOrganization(String organization) throws FreebaseServiceException;

    FreebaseSearchResult searchForMiscellaneous(String misc) throws FreebaseServiceException;

}
