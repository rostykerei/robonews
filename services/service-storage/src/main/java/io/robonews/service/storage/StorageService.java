/**
 * Robonews.io
 *
 * Copyright (c) 2013-2015 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.service.storage;

import java.io.File;
import java.io.IOException;
import java.util.Date;

public interface StorageService {

    void putFile(File source, String filename, String directory, String contentType, Date deleteAfterDate) throws IOException;

}
