/**
 * Robonews.io
 *
 * Copyright (c) 2013-2014 Rosty Kerei.
 * All rights reserved.
 */
package nl.rostykerei.news.worker.crawler.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import nl.rostykerei.news.dao.FeedDao;
import nl.rostykerei.news.domain.Feed;
import nl.rostykerei.news.messaging.domain.TagMessage;
import nl.rostykerei.news.service.http.HttpRequest;
import nl.rostykerei.news.service.http.HttpResponse;
import nl.rostykerei.news.service.http.HttpService;
import nl.rostykerei.news.service.syndication.SyndicationEntry;
import nl.rostykerei.news.service.syndication.SyndicationService;
import nl.rostykerei.news.service.syndication.SyndicationServiceParsingException;
import nl.rostykerei.news.worker.crawler.controller.impl.CrawlerControllerImpl;
import nl.rostykerei.news.worker.crawler.dao.CrawlerDao;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
public class CrawlerControllerTest {

    @Mock
    private FeedDao feedDao;

    @Mock
    private CrawlerDao crawlerDao;

    @Mock
    private HttpService httpService;

    @Mock
    private SyndicationService syndicationService;

    @Mock
    private RabbitTemplate messagingTemplate;

    @InjectMocks
    private CrawlerController crawlerController = new CrawlerControllerImpl();

    @Test
    public void testNoFeed() throws Exception {
        when(feedDao.pollFeedToProcess()).thenReturn(null);

        crawlerController.execute();

        verify(httpService, times(0)).execute(any(HttpRequest.class));
        verify(crawlerDao, times(0)).createStory(any(SyndicationEntry.class), any(Feed.class), any(Date.class));
        verify(messagingTemplate, times(0)).convertAndSend(any(TagMessage.class));
    }

    @Test
    public void testBadUrlFeed() throws Exception {
        Feed feed = new Feed();
        feed.setId(10);

        when(feedDao.pollFeedToProcess()).thenReturn(feed);
        when(httpService.execute(any(HttpRequest.class))).thenThrow(new IOException());

        crawlerController.execute();

        Assert.assertNotNull(feed.getPlannedCheck());
        verify(feedDao).update(feed);
    }

    @Test
    public void testNullHttpService() throws Exception {
        Feed feed = new Feed();
        feed.setId(10);

        when(feedDao.pollFeedToProcess()).thenReturn(feed);
        when(httpService.execute(any(HttpRequest.class))).thenReturn(null);

        crawlerController.execute();

        Assert.assertNotNull(feed.getPlannedCheck());
        verify(feedDao).update(feed);
    }

    @Test
    public void testBadFeedContent() throws Exception {
        Feed feed = new Feed();
        feed.setId(10);

        HttpResponse httpResponse = mock(HttpResponse.class);
        when(httpResponse.getHttpStatus()).thenReturn(200);

        when(feedDao.pollFeedToProcess()).thenReturn(feed);
        when(httpService.execute(any(HttpRequest.class))).thenReturn(httpResponse);

        when(syndicationService.loadFeed(any(InputStream.class))).thenThrow(
            new SyndicationServiceParsingException("test", new Exception())
        );

        crawlerController.execute();

        Assert.assertNotNull(feed.getPlannedCheck());
        verify(feedDao).update(feed);
    }

    @Test
    public void testNullSyndService() throws Exception {
        Feed feed = new Feed();
        feed.setId(10);

        HttpResponse httpResponse = mock(HttpResponse.class);
        when(httpResponse.getHttpStatus()).thenReturn(200);

        when(feedDao.pollFeedToProcess()).thenReturn(feed);
        when(httpService.execute(any(HttpRequest.class))).thenReturn(httpResponse);

        when(syndicationService.loadFeed(any(InputStream.class))).thenReturn(null);

        crawlerController.execute();

        Assert.assertNotNull(feed.getPlannedCheck());
        verify(feedDao).update(feed);
    }

    @Test
    public void testUnmodifiedContent() throws Exception {
        Feed feed = new Feed();
        feed.setId(10);

        HttpResponse httpResponse = mock(HttpResponse.class);
        when(httpResponse.getHttpStatus()).thenReturn(304);

        when(feedDao.pollFeedToProcess()).thenReturn(feed);
        when(httpService.execute(any(HttpRequest.class))).thenReturn(httpResponse);

        crawlerController.execute();

        Assert.assertNotNull(feed.getPlannedCheck());
        verify(feedDao).update(feed);
    }

    @Test
    public void testUnknownHttpStatus() throws Exception {
        Feed feed = new Feed();
        feed.setId(10);

        HttpResponse httpResponse = mock(HttpResponse.class);
        when(httpResponse.getHttpStatus()).thenReturn(500);

        when(feedDao.pollFeedToProcess()).thenReturn(feed);
        when(httpService.execute(any(HttpRequest.class))).thenReturn(httpResponse);

        crawlerController.execute();

        Assert.assertNotNull(feed.getPlannedCheck());
        verify(feedDao).update(feed);
    }
}
