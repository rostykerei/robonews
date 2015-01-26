/**
 * Robonews.io
 *
 * Copyright (c) 2013-2015 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.service.http;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@ContextConfiguration({ "classpath:serviceHttpContext-test.xml", "classpath:serviceHttpContext.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
public class HttpServiceTest {

    @Autowired
    private HttpService httpService;

    @Test
    public void testHttp() throws Exception {

        /* HttpRequest httpRequest = new HttpRequestImpl("http://localhost/data.php");
        httpRequest.setAccept("text/html,application/xhtml+xml,application/xml");
        httpRequest.setLastEtag("xxx");
        httpRequest.setLastModified(new Date());

        HttpResponse httpResponse = httpService.execute(httpRequest);

        String context = InputStreamReader.read(httpResponse.getStream());

        httpResponse.releaseConnection();

        System.out.print(context); */
    }

}
