import java.io.IOException;
import java.util.Date;
import nl.rostykerei.news.service.http.HttpRequest;
import nl.rostykerei.news.service.http.HttpResponse;
import nl.rostykerei.news.service.http.HttpService;
import nl.rostykerei.news.service.http.impl.HttpRequestImpl;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@ContextConfiguration({ "classpath:serviceContext.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
public class TempTest {

    @Autowired
    private HttpService httpService;

    @Test
    public void httpTest() {
        //HttpRequest httpRequest = new HttpRequestImpl("http://localhost/bbc.temp");
        HttpRequest httpRequest = new HttpRequestImpl("http://feeds.bbci.co.uk/news/rss.xml");
        httpRequest.setLastEtag("etag");
        httpRequest.setLastModified(new Date(new Date().getTime() - 31536000000L));

        try {
            System.out.println(new Date());
            HttpResponse httpResponse = httpService.execute(httpRequest);

            System.out.println(IOUtils.toString(httpResponse.getStream()));
            System.out.println(new Date());
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

}
