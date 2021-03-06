/**
 * Robonews.io
 *
 * Copyright (c) 2013-2015 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.service.storage.file;

import io.robonews.service.storage.StorageService;
import org.junit.Assert;
import org.junit.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Date;

public class StorageServiceFileTest {

    private static final String TEMP_DIR = System.getProperty("java.io.tmpdir");

    @Test
    public void testPutFile() throws Exception {
        StorageService storageService = new StorageServiceFile(TEMP_DIR);

        File tempFile = File.createTempFile("temp-file-", ".txt");
        tempFile.deleteOnExit();

        BufferedWriter bw = new BufferedWriter(new FileWriter(tempFile));
        bw.write("This is the temporary file content");
        bw.close();

        String dirName;

        do {
            dirName = Long.toString(Math.round(Math.random() * 100000000L));
        } while (
            new File(TEMP_DIR + File.separator + dirName).exists()
        );

        storageService.putFile(tempFile, "temp-copy.txt", dirName, null, new Date());

        File copyFile = new File(TEMP_DIR + File.separator + dirName + File.separator + "temp-copy.txt");

        Assert.assertTrue(copyFile.exists());
        Assert.assertTrue(copyFile.isFile());
        Assert.assertTrue(copyFile.canRead());
        Assert.assertTrue(copyFile.length() == tempFile.length());

        copyFile.delete();
        tempFile.delete();
        new File(TEMP_DIR + File.separator + dirName).delete();
    }
}
