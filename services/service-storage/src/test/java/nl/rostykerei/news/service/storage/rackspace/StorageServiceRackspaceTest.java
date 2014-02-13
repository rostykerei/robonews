package nl.rostykerei.news.service.storage.rackspace;

import nl.rostykerei.news.service.storage.StorageService;
import nl.rostykerei.news.service.storage.file.StorageServiceFile;
import org.junit.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Date;

public class StorageServiceRackspaceTest {

    @Test
    public void testPutFile() throws Exception {

        StorageService storageService = new StorageServiceRackspace();

        File tempFile = File.createTempFile("temp-file-", ".txt");
        tempFile.deleteOnExit();

        BufferedWriter bw = new BufferedWriter(new FileWriter(tempFile));
        bw.write("This is the temporary file content");
        bw.close();

        storageService.putFile(tempFile, "test88.txt", "140213", "plain/text", new Date( new Date().getTime() + 100000000000L ));

        tempFile.delete();


    }
}
