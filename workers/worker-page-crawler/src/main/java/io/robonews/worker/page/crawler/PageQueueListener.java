/**
 * Robonews.io
 *
 * Copyright (c) 2013-2015 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.worker.page.crawler;

import io.robonews.messaging.domain.ImageMessage;
import io.robonews.messaging.domain.PageMessage;
import io.robonews.service.http.HttpRequest;
import io.robonews.service.http.HttpResponse;
import io.robonews.service.http.HttpService;
import io.robonews.service.http.impl.HttpRequestImpl;
import io.robonews.service.text.tools.InputStreamReader;
import io.robonews.service.text.tools.MetaPropertyFetcher;
import org.apache.http.ContentTooLongException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.conn.ConnectTimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Set;

public class PageQueueListener {

    @Autowired
    private HttpService httpService;

    @Autowired
    @Qualifier("imageMessagingTemplate")
    private RabbitTemplate imageMessaging;

    private Logger logger = LoggerFactory.getLogger(PageQueueListener.class);

    public void listen(PageMessage message) {
        try {
            processMessage(message);
        }
        catch (ContentTooLongException e) {
            logger.info("Page content too long [" +  message.getPageUrl() + "], skipping...");
        }
        catch (ConnectTimeoutException | SocketTimeoutException e) {
            logger.info("Page does not respond [" +  message.getPageUrl() + "], skipping...");
        }
        catch (ClientProtocolException e) {
            logger.info("Page responds with HTTP error (too many redirects?) [" +  message.getPageUrl() + "], skipping...");
        }
        catch (IOException e) {
            logger.info("Page responds IOException (" + e.getMessage() + ") [" +  message.getPageUrl() + "], skipping...");
        }
        catch (Exception e) {
            logger.info("Failed to process PageMessage", e);
        }
    }

    private void processMessage(PageMessage message) throws IOException {
        HttpRequest httpRequest = new HttpRequestImpl(message.getPageUrl());
        httpRequest.setAccept("text/html,application/xhtml+xml,application/xml");

        HttpResponse httpResponse = null;
        String content;

        try {
            httpResponse = httpService.execute(httpRequest);

            if (httpResponse.getHttpStatus() != HttpResponse.STATUS_OK) {
                return;
            }

            content = InputStreamReader.read( httpResponse.getStream() );

            if (content == null || content.length() == 0) {
                return;
            }
        }
        finally {
            if (httpResponse != null) {
                httpResponse.releaseConnection();
            }
        }

        Set<String> imageUrls = MetaPropertyFetcher.fetchMetaImages(content);

        for (String imageUrl : imageUrls) {
            ImageMessage imageMessage = new ImageMessage();
            imageMessage.setStoryId(message.getStoryId());
            imageMessage.setImageUrl(imageUrl);

            imageMessaging.convertAndSend(imageMessage);
        }

    }
}
