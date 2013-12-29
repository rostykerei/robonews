package nl.rostykerei.news.worker.crawler.util;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created with IntelliJ IDEA on 8/8/13 at 3:46 PM
 *
 * @author Rosty Kerei
 */
public class SanitizeTest {

    @Test
    public void testNormalizeWhitespaces() {
        Assert.assertEquals("T e s t", Sanitizer.normalizeWhitespaces("  T \u200a e \t  s\n     t   "));
    }

    @Test
    public void testTruncate() {
        Assert.assertEquals("abc xyz…", Sanitizer.truncate("abc xyz qwe 123 ope", 10));
        Assert.assertEquals("abc xyz…", Sanitizer.truncate("abc xyz, qwe 123 ope", 10));
        Assert.assertEquals("abc xyz…", Sanitizer.truncate("abc xyz! Qwe 123 ope", 10));
        Assert.assertEquals("abc xyz q…", Sanitizer.truncate("abc xyz q? safdsf", 10));
        Assert.assertEquals("123456789…", Sanitizer.truncate("12345678901234567", 10));
    }
}
