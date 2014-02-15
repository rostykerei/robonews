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
import io.robonews.service.http.impl.HttpRequestImpl;
import io.robonews.service.text.tools.InputStreamReader;
import io.robonews.service.text.tools.MetaPropertyFetcher;
import org.apache.http.ContentTooLongException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.io.IOException;

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

            if (httpResponse.getHttpStatus() != 200) {
                return;
            }

            content = InputStreamReader.read( httpResponse.getStream() );

            if (content == null || content.length() == 0) {
                return;
            }
        }
        finally {
            synchronized (this) {
                if (httpResponse != null) {
                    httpResponse.releaseConnection();
                }
            }
        }

        String imageUrl = MetaPropertyFetcher.fetchOpenGraphImage(content);

        if (imageUrl != null) {
            ImageMessage imageMessage = new ImageMessage();
            imageMessage.setStoryId(message.getStoryId());
            imageMessage.setImageUrl(imageUrl);
            imageMessage.setDeadline(message.getDeadline());

            imageMessaging.convertAndSend(imageMessage);
        }
    }
}
