import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.feed.synd.SyndImage;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;
import nl.rostykerei.news.service.http.HttpRequest;
import nl.rostykerei.news.service.http.HttpResponse;
import nl.rostykerei.news.service.http.HttpService;
import nl.rostykerei.news.service.http.apache.HttpServiceApache;
import nl.rostykerei.news.service.http.impl.HttpRequestImpl;
import nl.rostykerei.news.service.syndication.SyndicationFeed;
import nl.rostykerei.news.service.syndication.SyndicationService;
import nl.rostykerei.news.service.syndication.SyndicationServiceException;
import nl.rostykerei.news.service.syndication.rome.SyndicationServiceRome;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class TempTest {

    @Test
    public void tes3Test() {
        HttpClient httpClient = new DefaultHttpClient();

        HttpService httpService = new HttpServiceApache(httpClient);
        HttpRequest httpRequest = new HttpRequestImpl("http://feeds.bbci.co.uk/news/world/rss.xml");

        SyndicationService syndicationService = new SyndicationServiceRome();
        try {
            HttpResponse httpResponse = httpService.execute(httpRequest);
            SyndicationFeed feed = syndicationService.loadFeed(httpResponse.getStream());
            System.out.println(feed.getTitle());

        }
        catch (SyndicationServiceException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }


    }

    @Test
    public void tes2Test() throws IOException {
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet("http://hc.apache.org/httpclient-3.x/apidocs/org/apache/commons/httpclient/class-use/Header.html");
        org.apache.http.HttpResponse response = httpClient.execute(httpGet);

        Header[] h= response.getAllHeaders();

        // Examine the response status
        System.out.println(response.getStatusLine());

        // Get hold of the response entity
        HttpEntity entity = response.getEntity();

        // If the response does not enclose an entity, there is no need
        // to worry about connection release
        if (entity != null) {
            InputStream instream = entity.getContent();
            try {

                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(instream));
                // do something useful with the response
                System.out.println(reader.readLine());

            } catch (IOException ex) {

                // In case of an IOException the connection will be released
                // back to the connection manager automatically
                throw ex;

            } catch (RuntimeException ex) {

                // In case of an unexpected exception you may want to abort
                // the HTTP request in order to shut down the underlying
                // connection and release it back to the connection manager.
                httpGet.abort();
                throw ex;

            } finally {

                // Closing the input stream will trigger connection release
                instream.close();

            }

            // When HttpClient instance is no longer needed,
            // shut down the connection manager to ensure
            // immediate deallocation of all system resources
            httpClient.getConnectionManager().shutdown();
        }
    }

    @Test
    public void tempTest() throws Exception{
        URL url  = new URL("http://localhost/rss/bbc.xml");
        XmlReader reader = null;

        try {

            reader = new XmlReader(url);
            SyndFeed feed = new SyndFeedInput().build(reader);

            Date dd = feed.getPublishedDate();
            System.out.println("Feed Title: "+ feed.getAuthor());

            String c = feed.getCopyright();
            SyndImage im = feed.getImage();

List l =feed.getCategories();
            for (Iterator i = feed.getEntries().iterator(); i.hasNext();) {
                SyndEntry entry = (SyndEntry) i.next();

                String u = entry.getUri();
                System.out.println(entry.getPublishedDate().toString() + " " + entry.getTitle());

            }
        }
        catch (Exception e) {
            System.out.println(e.toString());
        }
        finally {
            if (reader != null)
                reader.close();
        }
    }
}
