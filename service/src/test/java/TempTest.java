import nl.rostykerei.news.service.http.HttpService;
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
                /*
    @Test
    public void httpTest() {
        //HttpRequest httpRequest = new HttpRequestImpl("http://localhost/bbc.temp");
        HttpRequest httpRequest = new HttpRequestImpl("http://www.telegraph.co.uk/news/uknews/rss");
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
                                       */
/*
    @Test
    public void freebaseTest() {
        try {

            HttpTransport httpTransport = new NetHttpTransport();
            HttpRequestFactory requestFactory = httpTransport.createRequestFactory();
            JSONParser parser = new JSONParser();
            GenericUrl url = new GenericUrl("https://www.googleapis.com/freebase/v1/search");
            url.put("query", "NBA");
            url.put("filter", "(should type:/organization/)");
            url.put("limit", "1");
            //url.put("indent", "true");
            url.put("key", "AIzaSyC4COoAiufFHk9VGbSRhIZYL9kVVmjUFeI");
            HttpRequest request = requestFactory.buildGetRequest(url);
            HttpResponse httpResponse = request.execute();
            JSONObject response = (JSONObject)parser.parse(httpResponse.parseAsString());
            JSONArray results = (JSONArray)response.get("result");
            for (Object result : results) {
                System.out.println(JsonPath.read(result, "$.name").toString());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }    */

}
