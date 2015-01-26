/**
 * Robonews.io
 *
 * Copyright (c) 2013-2015 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.service.freebase;

import io.robonews.service.freebase.exception.FreebaseServiceException;
import io.robonews.service.freebase.impl.FreebaseSearchResult;

public interface FreebaseService {

    FreebaseSearchResult searchPerson(String query) throws FreebaseServiceException;

    FreebaseSearchResult searchLocation(String query) throws FreebaseServiceException;

    FreebaseSearchResult searchOrganization(String query) throws FreebaseServiceException;

    FreebaseSearchResult searchMisc(String query) throws FreebaseServiceException;

}
