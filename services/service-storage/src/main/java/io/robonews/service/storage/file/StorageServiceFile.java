/**
 * Robonews.io
 *
 * Copyright (c) 2013-2014 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.service.storage.file;

import io.robonews.service.storage.StorageService;

import java.io.*;
import java.util.Date;


public class StorageServiceFile implements StorageService {

    private String rootDir;

    public StorageServiceFile(String rootDir) {
        if (rootDir != null && !rootDir.endsWith(File.separator)) {
            rootDir += File.separator;
        }

        this.rootDir = rootDir;
    }

    @Override
    public void putFile(File source, String filename, String directory, String contentType, Date deleteAfterDate) throws IOException {

        synchronized (this) {
            if (!new File(rootDir + directory).exists()) {
                boolean dirSuccess = new File(rootDir + directory).mkdirs();

                if (!dirSuccess) {
                    throw new IOException("Could not create directory: " + rootDir + directory);
                }
            }
        }

        InputStream is = null;
        OutputStream os = null;
        try {
            is = new FileInputStream(source);
            os = new FileOutputStream(new File(rootDir + directory + File.separator + filename));
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        } finally {
            is.close();
            os.close();
        }
    }
}
