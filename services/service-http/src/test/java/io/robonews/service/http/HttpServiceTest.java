/**
 * Robonews.io
 *
 * Copyright (c) 2013-2015 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.service.http;

import io.robonews.service.http.impl.HttpRequestImpl;
import io.robonews.service.text.tools.InputStreamReader;
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

        HttpRequest httpRequest = new HttpRequestImpl("http://nypost.com/feed");
       // httpRequest.setAccept("text/html,application/xhtml+xml,application/xml");


        HttpResponse httpResponse = httpService.execute(httpRequest);

        String context = InputStreamReader.read(httpResponse.getStream());

        httpResponse.releaseConnection();

        System.out.print(context);
    }

}
