/**
 * Robonews.io
 *
 * Copyright (c) 2013-2015 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.service.alexa;

import io.robonews.service.alexa.exception.AlexaServiceException;
import io.robonews.service.alexa.impl.AlexaServiceResult;

/**
 * Created by rosty on 11/5/14.
 */
public interface AlexaService {

    AlexaServiceResult query(String url) throws AlexaServiceException;

}
