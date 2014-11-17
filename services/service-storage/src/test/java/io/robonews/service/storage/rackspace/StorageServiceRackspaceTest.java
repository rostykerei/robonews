/**
 * Robonews.io
 *
 * Copyright (c) 2013-2014 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.service.storage.rackspace;

import io.robonews.service.storage.StorageService;
import org.junit.Ignore;
import org.junit.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Date;

public class StorageServiceRackspaceTest {

    @Test
    @Ignore
    public void testPutFile() throws Exception {

        StorageService storageService = new StorageServiceRackspace("robonews.files", "c8de7e43df9349a2a2619aaec54b7c74", "test");

        File tempFile = File.createTempFile("temp-file-", ".txt");
        tempFile.deleteOnExit();

        BufferedWriter bw = new BufferedWriter(new FileWriter(tempFile));
        bw.write("This is the temporary file content");
        bw.close();

        storageService.putFile(tempFile, "test88.txt", "140213", "plain/text", new Date( new Date().getTime() + 100000000000L ));

        tempFile.delete();


    }
}
