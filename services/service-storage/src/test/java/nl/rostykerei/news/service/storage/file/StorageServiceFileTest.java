package nl.rostykerei.news.service.storage.file;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Date;
import org.junit.Assert;
import org.junit.Test;

public class StorageServiceFileTest {

    private static final String TEMP_DIR = System.getProperty("java.io.tmpdir");

    @Test
    public void testPutFile() throws Exception {
        StorageServiceFile storageService = new StorageServiceFile(TEMP_DIR);

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

        storageService.putFile(tempFile, "temp-copy.txt", dirName, new Date());

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
