/**
 * Robonews.io
 *
 * Copyright (c) 2013-2014 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.service.alexa.impl;

import io.robonews.service.alexa.AlexaService;
import io.robonews.service.alexa.exception.AlexaServiceException;
import io.robonews.service.http.HttpRequest;
import io.robonews.service.http.HttpResponse;
import io.robonews.service.http.HttpService;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AlexaServiceImplTest extends TestCase {

    @Mock
    private HttpService httpService;

    @InjectMocks
    private AlexaService alexaService = new AlexaServiceImpl(httpService);

    @Test
    public void testQuery() throws Exception {
        HttpResponse httpResponse = mock(HttpResponse.class);
        when(httpResponse.getHttpStatus()).thenReturn(HttpResponse.STATUS_OK);
        when(httpResponse.getStream()).thenReturn(new ByteArrayInputStream(
                ("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                    "\n" +
                    "<ALEXA VER=\"0.9\" URL=\"cnn.com/\" HOME=\"0\" AID=\"=\" IDN=\"cnn.com/\">\n" +
                    "\n" +
                    "<KEYWORDS>\n" +
                    "<KEYWORD VAL=\"Networks\"/>\n" +
                    "<KEYWORD VAL=\"Cable\"/>\n" +
                    "<KEYWORD VAL=\"CNN News Group\"/>\n" +
                    "</KEYWORDS><DMOZ>\n" +
                    "<SITE BASE=\"cnn.com/\" TITLE=\"CNN Interactive\" DESC=\"News, weather, sports, and services including e-mail news alerts and downloadable audio/video reports.\">\n" +
                    "<CATS>\n" +
                    "<CAT ID=\"Top/News\" TITLE=\"English/News\" CID=\"998601\"/>\n" +
                    "<CAT ID=\"Top/Arts/Television/News\" TITLE=\"Television/News\" CID=\"395700\"/>\n" +
                    "<CAT ID=\"Top/Arts/Television/Networks/Cable/CNN\" TITLE=\"Cable/CNN\" CID=\"392942\"/>\n" +
                    "</CATS>\n" +
                    "</SITE>\n" +
                    "</DMOZ>\n" +
                    "<SD>\n" +
                    "<POPULARITY URL=\"cnn.com/\" TEXT=\"62\" SOURCE=\"panel\"/>\n" +
                    "<REACH RANK=\"57\"/>\n" +
                    "<RANK DELTA=\"-1\"/>\n" +
                    "<COUNTRY CODE=\"US\" NAME=\"United States\" RANK=\"23\"/>\n" +
                    "</SD>\n" +
                    "</ALEXA>").getBytes()));

        when(httpService.execute(any(HttpRequest.class))).thenReturn(httpResponse);

        AlexaServiceResult res = alexaService.query("cnn.com");

        Assert.assertEquals("cnn.com", res.getSiteBase());
        Assert.assertEquals("CNN Interactive", res.getTitle());
        Assert.assertEquals("News, weather, sports, and services including e-mail news alerts and downloadable audio/video reports.", res.getDescription());
        Assert.assertEquals(62, res.getRank());
    }

    @Test(expected = AlexaServiceException.class)
    public void testHttpErrorResponse() throws Exception {
        HttpResponse httpResponse = mock(HttpResponse.class);
        when(httpResponse.getHttpStatus()).thenReturn(HttpResponse.STATUS_INTERNAL_SERVER_ERROR);
        when(httpService.execute(any(HttpRequest.class))).thenReturn(httpResponse);

        alexaService.query("cnn.com");
    }

    @Test(expected = AlexaServiceException.class)
    public void testHttpExceptionResponse() throws Exception {
        when(httpService.execute(any(HttpRequest.class))).thenThrow(new IOException("testy"));
        alexaService.query("cnn.com");
    }

    @Test(expected = AlexaServiceException.class)
    public void test404ResponseQuery() throws Exception {
        HttpResponse httpResponse = mock(HttpResponse.class);
        when(httpResponse.getHttpStatus()).thenReturn(HttpResponse.STATUS_OK);
        when(httpResponse.getStream()).thenReturn(new ByteArrayInputStream(
                ("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                        "\n" +
                        "<ALEXA VER=\"0.9\" URL=\"404\" HOME=\"0\" AID=\"=\" IDN=\"\">\n" +
                        "\n" +
                        "</ALEXA>").getBytes()));

        when(httpService.execute(any(HttpRequest.class))).thenReturn(httpResponse);

        alexaService.query("cnn.zzz");
    }

    @Test(expected = AlexaServiceException.class)
    public void testEmptyResponseQuery() throws Exception {
        HttpResponse httpResponse = mock(HttpResponse.class);
        when(httpResponse.getHttpStatus()).thenReturn(HttpResponse.STATUS_OK);
        when(httpResponse.getStream()).thenReturn(new ByteArrayInputStream(
                ("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                        "\n" +
                        "<ALEXA VER=\"0.9\" URL=\"zz.robonews.io/\" HOME=\"0\" AID=\"=\" IDN=\"zz.robonews.io/\">\n" +
                        "\n" +
                        "</ALEXA>").getBytes()));

        when(httpService.execute(any(HttpRequest.class))).thenReturn(httpResponse);

        alexaService.query("zz.robonews.io");
    }

    @Test(expected = AlexaServiceException.class)
    public void testBadRank() throws Exception {
        HttpResponse httpResponse = mock(HttpResponse.class);
        when(httpResponse.getHttpStatus()).thenReturn(HttpResponse.STATUS_OK);
        when(httpResponse.getStream()).thenReturn(new ByteArrayInputStream(
                ("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                        "\n" +
                        "<ALEXA VER=\"0.9\" URL=\"cnn.com/\" HOME=\"0\" AID=\"=\" IDN=\"cnn.com/\">\n" +
                        "\n" +
                        "<KEYWORDS>\n" +
                        "<KEYWORD VAL=\"Networks\"/>\n" +
                        "<KEYWORD VAL=\"Cable\"/>\n" +
                        "<KEYWORD VAL=\"CNN News Group\"/>\n" +
                        "</KEYWORDS><DMOZ>\n" +
                        "<SITE BASE=\"cnn.com/\" TITLE=\"CNN Interactive\" DESC=\"News, weather, sports, and services including e-mail news alerts and downloadable audio/video reports.\">\n" +
                        "<CATS>\n" +
                        "<CAT ID=\"Top/News\" TITLE=\"English/News\" CID=\"998601\"/>\n" +
                        "<CAT ID=\"Top/Arts/Television/News\" TITLE=\"Television/News\" CID=\"395700\"/>\n" +
                        "<CAT ID=\"Top/Arts/Television/Networks/Cable/CNN\" TITLE=\"Cable/CNN\" CID=\"392942\"/>\n" +
                        "</CATS>\n" +
                        "</SITE>\n" +
                        "</DMOZ>\n" +
                        "<SD>\n" +
                        "<POPULARITY URL=\"\" TEXT=\"BAD NUMBER\" SOURCE=\"panel\"/>\n" +
                        "<REACH RANK=\"57\"/>\n" +
                        "<RANK DELTA=\"-1\"/>\n" +
                        "<COUNTRY CODE=\"US\" NAME=\"United States\" RANK=\"23\"/>\n" +
                        "</SD>\n" +
                        "</ALEXA>").getBytes()));

        when(httpService.execute(any(HttpRequest.class))).thenReturn(httpResponse);

        alexaService.query("cnn.com");
    }


    @Test(expected = AlexaServiceException.class)
    public void testBadUrl() throws Exception {
        HttpResponse httpResponse = mock(HttpResponse.class);
        when(httpResponse.getHttpStatus()).thenReturn(HttpResponse.STATUS_OK);
        when(httpResponse.getStream()).thenReturn(new ByteArrayInputStream(
                ("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                        "\n" +
                        "<ALEXA VER=\"0.9\" URL=\"cnn.com/\" HOME=\"0\" AID=\"=\" IDN=\"cnn.com/\">\n" +
                        "\n" +
                        "<KEYWORDS>\n" +
                        "<KEYWORD VAL=\"Networks\"/>\n" +
                        "<KEYWORD VAL=\"Cable\"/>\n" +
                        "<KEYWORD VAL=\"CNN News Group\"/>\n" +
                        "</KEYWORDS><DMOZ>\n" +
                        "<SITE BASE=\"cnn.com/\" TITLE=\"CNN Interactive\" DESC=\"News, weather, sports, and services including e-mail news alerts and downloadable audio/video reports.\">\n" +
                        "<CATS>\n" +
                        "<CAT ID=\"Top/News\" TITLE=\"English/News\" CID=\"998601\"/>\n" +
                        "<CAT ID=\"Top/Arts/Television/News\" TITLE=\"Television/News\" CID=\"395700\"/>\n" +
                        "<CAT ID=\"Top/Arts/Television/Networks/Cable/CNN\" TITLE=\"Cable/CNN\" CID=\"392942\"/>\n" +
                        "</CATS>\n" +
                        "</SITE>\n" +
                        "</DMOZ>\n" +
                        "<SD>\n" +
                        "<POPULARITY URL=\"\" TEXT=\"777\" SOURCE=\"panel\"/>\n" +
                        "<REACH RANK=\"57\"/>\n" +
                        "<RANK DELTA=\"-1\"/>\n" +
                        "<COUNTRY CODE=\"US\" NAME=\"United States\" RANK=\"23\"/>\n" +
                        "</SD>\n" +
                        "</ALEXA>").getBytes()));

        when(httpService.execute(any(HttpRequest.class))).thenReturn(httpResponse);

        alexaService.query("cnn.com");
    }
}