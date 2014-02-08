package nl.rostykerei.news.worker.image.crawler;

import org.junit.Test;

public class ImageQueueListenerTest {

    @Test
    public void testListen() throws Exception {
        try {
            System.out.println("1");
            throw new RuntimeException();
        }
        finally {
            System.out.println("2");
        }
    }
}
