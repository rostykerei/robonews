/**
 * Robonews.io
 *
 * Copyright (c) 2013-2015 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.worker.crawler;

import io.robonews.dao.FeedDao;
import io.robonews.domain.Feed;
import io.robonews.messaging.domain.CrawlMessage;
import io.robonews.messaging.domain.TagMessage;
import io.robonews.service.http.HttpRequest;
import io.robonews.service.http.HttpResponse;
import io.robonews.service.http.HttpService;
import io.robonews.service.syndication.SyndicationEntry;
import io.robonews.service.syndication.SyndicationService;
import io.robonews.service.syndication.SyndicationServiceParsingException;
import io.robonews.worker.crawler.dao.CrawlerDao;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
public class CrawlerTest {

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
    private Crawler crawler = new Crawler();

    @Test
    public void testBadUrlFeed() throws Exception {
        Feed feed = new Feed();
        feed.setId(10);

        when(feedDao.getById(10)).thenReturn(feed);
        when(httpService.execute(any(HttpRequest.class))).thenThrow(new IOException());

        crawler.listen(new CrawlMessage(10));

        Assert.assertNotNull(feed.getPlannedCheck());
        verify(feedDao).update(feed);
    }

    @Test
    public void testNullHttpService() throws Exception {
        Feed feed = new Feed();
        feed.setId(10);

        when(feedDao.getById(10)).thenReturn(feed);
        when(httpService.execute(any(HttpRequest.class))).thenReturn(null);

        crawler.listen(new CrawlMessage(10));

        Assert.assertNotNull(feed.getPlannedCheck());
        verify(feedDao).update(feed);
    }

    @Test
    public void testBadFeedContent() throws Exception {
        Feed feed = new Feed();
        feed.setId(10);

        HttpResponse httpResponse = mock(HttpResponse.class);
        when(httpResponse.getHttpStatus()).thenReturn(HttpResponse.STATUS_OK);

        when(feedDao.getById(10)).thenReturn(feed);
        when(httpService.execute(any(HttpRequest.class))).thenReturn(httpResponse);

        when(syndicationService.loadFeed(any(InputStream.class))).thenThrow(
            new SyndicationServiceParsingException("test", new Exception())
        );

        crawler.listen(new CrawlMessage(10));

        Assert.assertNotNull(feed.getPlannedCheck());
        verify(feedDao).update(feed);
    }

    @Test
    public void testNullSyndService() throws Exception {
        Feed feed = new Feed();
        feed.setId(10);

        HttpResponse httpResponse = mock(HttpResponse.class);
        when(httpResponse.getHttpStatus()).thenReturn(HttpResponse.STATUS_OK);

        when(feedDao.getById(10)).thenReturn(feed);
        when(httpService.execute(any(HttpRequest.class))).thenReturn(httpResponse);

        when(syndicationService.loadFeed(any(InputStream.class))).thenReturn(null);

        crawler.listen(new CrawlMessage(10));

        Assert.assertNotNull(feed.getPlannedCheck());
        verify(feedDao).update(feed);
    }

    @Test
    public void testUnmodifiedContent() throws Exception {
        Feed feed = new Feed();
        feed.setId(10);

        HttpResponse httpResponse = mock(HttpResponse.class);
        when(httpResponse.getHttpStatus()).thenReturn(HttpResponse.STATUS_NOT_MODIFIED);

        when(feedDao.getById(10)).thenReturn(feed);
        when(httpService.execute(any(HttpRequest.class))).thenReturn(httpResponse);

        crawler.listen(new CrawlMessage(10));

        Assert.assertNotNull(feed.getPlannedCheck());
        verify(feedDao).update(feed);
    }

    @Test
    public void testUnknownHttpStatus() throws Exception {
        Feed feed = new Feed();
        feed.setId(10);

        HttpResponse httpResponse = mock(HttpResponse.class);
        when(httpResponse.getHttpStatus()).thenReturn(HttpResponse.STATUS_INTERNAL_SERVER_ERROR);

        when(feedDao.getById(10)).thenReturn(feed);
        when(httpService.execute(any(HttpRequest.class))).thenReturn(httpResponse);

        crawler.listen(new CrawlMessage(10));

        Assert.assertNotNull(feed.getPlannedCheck());
        verify(feedDao).update(feed);
    }
}
