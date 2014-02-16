/**
 * Robonews.io
 *
 * Copyright (c) 2013-2014 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.worker.page.crawler;

import io.robonews.messaging.domain.ImageMessage;
import io.robonews.messaging.domain.PageMessage;
import io.robonews.service.http.HttpRequest;
import io.robonews.service.http.HttpResponse;
import io.robonews.service.http.HttpService;
import org.apache.commons.io.IOUtils;
import org.apache.http.ContentTooLongException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.conn.ConnectTimeoutException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Date;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class PageQueueListenerTest {

    private static final String VALID_HTML = "<!DOCTYPE html>\n" +
            "<html lang=\"en\">\n" +
            "<head>\n" +
            "<meta charset=\"UTF-8\"/>\n" +
            "<meta property=\"og:image\" content=\"http://i.robonews.io/image.jpg\"/>\n" +
            "</head>\n" +
            "<body>\n" +
            "<h1>www.robonews.io</h1>\n" +
            "</body>\n" +
            "</html>";
    @Mock
    private HttpService httpService;

    @Mock
    private RabbitTemplate imageMessaging;

    @InjectMocks
    private PageQueueListener pageQueueListener = new PageQueueListener();

    @Test
    public void testPageQueueListener() throws Exception {
        final Date deadline = new Date( new Date().getTime() + 900000L);

        PageMessage pageMessage = new PageMessage();
        pageMessage.setPageUrl("http://www.robonews.io/test.html");
        pageMessage.setStoryId(100);
        pageMessage.setDeadline(deadline);

        HttpResponse httpResponse = mock(HttpResponse.class);

        when(httpResponse.getHttpStatus()).thenReturn(200);
        when(httpResponse.getStream()).thenReturn(IOUtils.toInputStream(VALID_HTML, "UTF-8"));

        when(
            httpService.execute(argThat(new ArgumentMatcher<HttpRequest>() {
                @Override
                public boolean matches(Object argument) {
                    return ((HttpRequest) argument)
                        .getUrl()
                        .equals("http://www.robonews.io/test.html");
                }
            }))
        ).thenReturn(httpResponse);

        pageQueueListener.listen(pageMessage);

        verify(httpResponse, times(1)).releaseConnection();

        verify(imageMessaging).convertAndSend( argThat(new ArgumentMatcher<ImageMessage>() {
            @Override
            public boolean matches(Object argument) {
                ImageMessage msg = (ImageMessage) argument;
                return msg.getImageUrl().equals("http://i.robonews.io/image.jpg") &&
                    msg.getStoryId() == 100 &&
                    msg.getDeadline().equals(deadline);
            }
        }) );
    }

    @Test
    public void testPageQueueListener404() throws Exception {
        final Date deadline = new Date( new Date().getTime() + 900000L);

        PageMessage pageMessage = new PageMessage();
        pageMessage.setPageUrl("http://www.robonews.io/test.html");
        pageMessage.setStoryId(100);
        pageMessage.setDeadline(deadline);

        HttpResponse httpResponse = mock(HttpResponse.class);

        when(httpResponse.getHttpStatus()).thenReturn(404);
        when(httpResponse.getStream()).thenReturn(IOUtils.toInputStream("", "UTF-8"));

        when(httpService.execute(any(HttpRequest.class))).thenReturn(httpResponse);

        pageQueueListener.listen(pageMessage);

        verify(httpResponse, times(1)).releaseConnection();

        verify(imageMessaging, times(0)).convertAndSend(any());
    }

    @Test
    public void testPageQueueListenerBlankResponse() throws Exception {
        final Date deadline = new Date( new Date().getTime() + 900000L);

        PageMessage pageMessage = new PageMessage();
        pageMessage.setPageUrl("http://www.robonews.io/test.html");
        pageMessage.setStoryId(100);
        pageMessage.setDeadline(deadline);

        HttpResponse httpResponse = mock(HttpResponse.class);

        when(httpResponse.getHttpStatus()).thenReturn(200);
        when(httpResponse.getStream()).thenReturn(IOUtils.toInputStream("", "UTF-8"));

        when(httpService.execute(any(HttpRequest.class))).thenReturn(httpResponse);

        pageQueueListener.listen(pageMessage);

        verify(httpResponse, times(1)).releaseConnection();

        verify(imageMessaging, times(0)).convertAndSend(any());
    }

    @Test
    public void testPageQueueListenerConnectTimeoutException() throws Exception {
        final Date deadline = new Date( new Date().getTime() + 900000L);

        PageMessage pageMessage = new PageMessage();
        pageMessage.setPageUrl("http://www.robonews.io/test.html");
        pageMessage.setStoryId(100);
        pageMessage.setDeadline(deadline);

        when(httpService.execute(any(HttpRequest.class))).thenThrow(new ConnectTimeoutException("test"));

        pageQueueListener.listen(pageMessage);

        verify(imageMessaging, times(0)).convertAndSend(any());
    }

    @Test
    public void testPageQueueListenerSocketTimeoutException() throws Exception {
        final Date deadline = new Date( new Date().getTime() + 900000L);

        PageMessage pageMessage = new PageMessage();
        pageMessage.setPageUrl("http://www.robonews.io/test.html");
        pageMessage.setStoryId(100);
        pageMessage.setDeadline(deadline);

        when(httpService.execute(any(HttpRequest.class))).thenThrow(new SocketTimeoutException("test"));

        pageQueueListener.listen(pageMessage);

        verify(imageMessaging, times(0)).convertAndSend(any());
    }

    @Test
    public void testPageQueueListenerClientProtocolException() throws Exception {
        final Date deadline = new Date( new Date().getTime() + 900000L);

        PageMessage pageMessage = new PageMessage();
        pageMessage.setPageUrl("http://www.robonews.io/test.html");
        pageMessage.setStoryId(100);
        pageMessage.setDeadline(deadline);

        when(httpService.execute(any(HttpRequest.class))).thenThrow(new ClientProtocolException("test"));

        pageQueueListener.listen(pageMessage);

        verify(imageMessaging, times(0)).convertAndSend(any());
    }

    @Test
    public void testPageQueueListenerContentTooLongException() throws Exception {
        final Date deadline = new Date( new Date().getTime() + 900000L);

        PageMessage pageMessage = new PageMessage();
        pageMessage.setPageUrl("http://www.robonews.io/test.html");
        pageMessage.setStoryId(100);
        pageMessage.setDeadline(deadline);

        when(httpService.execute(any(HttpRequest.class))).thenThrow(new ContentTooLongException("test"));

        pageQueueListener.listen(pageMessage);

        verify(imageMessaging, times(0)).convertAndSend(any());
    }

    @Test
    public void testPageQueueListenerIOException() throws Exception {
        final Date deadline = new Date( new Date().getTime() + 900000L);

        PageMessage pageMessage = new PageMessage();
        pageMessage.setPageUrl("http://www.robonews.io/test.html");
        pageMessage.setStoryId(100);
        pageMessage.setDeadline(deadline);

        when(httpService.execute(any(HttpRequest.class))).thenThrow(new IOException("test"));

        pageQueueListener.listen(pageMessage);

        verify(imageMessaging, times(0)).convertAndSend(any());
    }

    @Test
    public void testPageQueueListenerException() throws Exception {
        final Date deadline = new Date( new Date().getTime() + 900000L);

        PageMessage pageMessage = new PageMessage();
        pageMessage.setPageUrl("http://www.robonews.io/test.html");
        pageMessage.setStoryId(100);
        pageMessage.setDeadline(deadline);

        when(httpService.execute(any(HttpRequest.class))).thenThrow(new RuntimeException("test"));

        pageQueueListener.listen(pageMessage);

        verify(imageMessaging, times(0)).convertAndSend(any());
    }
}
