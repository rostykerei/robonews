package nl.rostykerei.news.service.phash;

import org.junit.Test;

import java.io.FileInputStream;

/**
 * Created with IntelliJ IDEA.
 * User: rosty
 * Date: 1/5/14
 * Time: 8:18 PM
 * To change this template use File | Settings | File Templates.
 */
public class ImagePHashTest {

    @Test
    public void testGetHash() throws Exception {

        ImagePHash imagePHash = new ImagePHash();
        FileInputStream f1 = new FileInputStream("/home/rosty/test/phash/d1.jpg");
        FileInputStream f2 = new FileInputStream("/home/rosty/test/phash/d2.jpg");

        String h1 = imagePHash.hashSimple(f1);
        String h2 = imagePHash.hashSimple(f2);

        System.out.println(h1);
        System.out.println(h2);

        int d = getHammingDistance(h1, h2);
        System.out.println("DIFF: " + d + " (likeness " + (100-100*d/64) + "%)");

    }

    public static int getHammingDistance(String sequence1, String sequence2) {
        int a = 0;
        for (int x = 0; x < sequence1.length(); x++) { //both are of the same length
            if (sequence1.charAt(x) != sequence2.charAt(x)) {
                a += 1;
            }
        }

        return a;
    }
}
