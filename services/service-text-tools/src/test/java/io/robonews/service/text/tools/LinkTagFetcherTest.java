package io.robonews.service.text.tools;

import org.junit.Assert;
import org.junit.Test;

import java.util.Map;

/**
 * Created by rosty on 04/05/15.
 */
public class LinkTagFetcherTest {

    @Test
    public void test1() {
        Map<String, String> res =  LinkTagFetcher
                .fetchLinkTags("<meta http-equiv=\"pragma\" content=\"no-cache\" />\n" +
                        "<link rel=\"icon\" href=\"http://global.fncstatic.com/static/v/fn-hp/img/favicon.png\" type=\"image/png\">\n" +
                        "<link href=\"http://global.fncstatic.com/static/v/fn-hp/img/favicon.ico\" rel=\"shortcut icon\">\n" +
                        "<link rel=\"apple-touch-icon\" size=\"111x111\" href=\"/apple-touch-icon.png\">");

        Assert.assertEquals(3, res.size());
        Assert.assertEquals("http://global.fncstatic.com/static/v/fn-hp/img/favicon.png", res.get("icon"));
        Assert.assertEquals("http://global.fncstatic.com/static/v/fn-hp/img/favicon.ico", res.get("shortcut icon"));
        Assert.assertEquals("/apple-touch-icon.png", res.get("apple-touch-icon"));
    }

}