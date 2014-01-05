package nl.rostykerei.news.service.syndication;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.net.URL;
import java.net.URLConnection;

/**
 * Created with IntelliJ IDEA.
 * User: rosty
 * Date: 1/4/14
 * Time: 10:27 PM
 * To change this template use File | Settings | File Templates.
 */
@ContextConfiguration({ "classpath:serviceSyndicationContext.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
public class SyndicationServiceTest {

    @Autowired
    private SyndicationService syndicationService;

    @Test
    public void test() throws Exception {
        // URL url = new URL("http://feeds.theguardian.com/theguardian/uk/culture/rss");
        URL url = new URL("http://feeds.bbci.co.uk/news/rss.xml");
        URLConnection conn = url.openConnection();

        SyndicationFeed syndicationFeed = syndicationService.loadFeed(conn.getInputStream());

        syndicationFeed.getEntries();

        for (SyndicationEntry syndicationEntry : syndicationFeed.getEntries()) {
            System.out.println(syndicationEntry.getTitle() +"----" +syndicationEntry.getMediaKeywords());
        }



    }

}
