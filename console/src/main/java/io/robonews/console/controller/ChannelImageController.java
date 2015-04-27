/**
 * Robonews.io
 *
 * Copyright (c) 2013-2015 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.console.controller;

import io.robonews.console.controller.error.BadRequestException;
import io.robonews.console.controller.error.NotFoundException;
import io.robonews.console.dto.channel.ChannelImageOption;
import io.robonews.dao.ChannelDao;
import io.robonews.domain.Channel;
import io.robonews.domain.ChannelPicture;
import io.robonews.domain.Image;
import io.robonews.service.facebook.FacebookService;
import io.robonews.service.google.plus.GooglePlusService;
import io.robonews.service.http.HttpRequest;
import io.robonews.service.http.HttpResponse;
import io.robonews.service.http.HttpService;
import io.robonews.service.http.impl.HttpRequestImpl;
import io.robonews.service.twitter.TwitterService;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
@RequestMapping("rest")
public class ChannelImageController {

    @Autowired
    private ChannelDao channelDao;

    @Autowired
    private HttpService httpService;

    @Autowired
    private FacebookService facebookService;

    @Autowired
    private TwitterService twitterService;

    @Autowired
    private GooglePlusService googlePlusService;

    @RequestMapping(value = "/channel/image-options/{channelId}")
    public @ResponseBody List<ChannelImageOption> getImageOptions(@PathVariable int channelId) {
        Channel channel = channelDao.getById(channelId);

        if (channel == null) {
            throw new NotFoundException();
        }

        return Stream.of("Facebook", "Twitter", "Google+")
            .parallel()
            .map(i -> getImageOption(channel, i))
            .filter(i -> i != null)
            .collect(Collectors.toList());
    }

    @RequestMapping(value = "/channel/image-update/{channelId}", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void updateImage(@PathVariable int channelId,
                            @RequestParam("type") String type, @RequestParam("data") String data) {

        Channel channel = channelDao.getById(channelId);

        if (channel == null) {
            throw new NotFoundException();
        }

        if (StringUtils.isEmpty(type) || StringUtils.isEmpty(data)) {
            throw new BadRequestException("Parameters type and data cannot be empty");
        }

        Image.Type imageType;

        switch (type) {
            case "jpg":
            case "jpeg":
                imageType = Image.Type.JPEG;
                break;
            case "png":
                imageType = Image.Type.PNG;
                break;
            case "gif":
                imageType = Image.Type.GIF;
                break;
            default:
                throw new BadRequestException("Unknown image type");
        }

        byte[] imageBytes = Base64.decodeBase64(data);

        ChannelPicture picture = channel.getPicture() == null ? new ChannelPicture() : channel.getPicture();

        picture.setChannel(channel);
        picture.setPicture(imageBytes);
        picture.setType(imageType);

        channel.setPicture(picture);

        channelDao.update(channel);

    }

    private ChannelImageOption getImageOption(Channel channel, String socialMedia) {
        String url = null;

        switch (socialMedia) {
            case "Facebook":
                if (!StringUtils.isEmpty(channel.getFacebookId())) {
                    url = facebookService.getProfilePictureUrl(channel.getFacebookId());
                }
                break;
            case "Twitter":
                if (!StringUtils.isEmpty(channel.getTwitterId())) {
                    url = twitterService.getProfilePictureUrl(channel.getTwitterId());
                }
                break;
            case "Google+":
                if (!StringUtils.isEmpty(channel.getGooglePlusId())) {
                    url = googlePlusService.getProfilePictureUrl(channel.getGooglePlusId());
                }
                break;
        }

        if (url == null) {
            return null;
        }

        HttpRequest httpRequest = new HttpRequestImpl(url);
        httpRequest.setAccept("image/png,image/*;q=0.8,*/*;q=0.5");

        try {
            HttpResponse httpResponse = httpService.execute(httpRequest);

            if (httpResponse != null && httpResponse.getHttpStatus() == 200
                    && httpResponse.getContentType() != null && httpResponse.getContentType().startsWith("image/")) {

                ChannelImageOption imageOption = new ChannelImageOption();
                imageOption.setSource(socialMedia);
                imageOption.setType(httpResponse.getContentType());

                String base64data = Base64.encodeBase64String(
                        IOUtils.toByteArray(
                                httpResponse.getStream()
                        )
                );

                imageOption.setData(base64data);

                return imageOption;
            }
        }
        catch (Exception e) {
            return null;
        }

        return null;
    }

}
