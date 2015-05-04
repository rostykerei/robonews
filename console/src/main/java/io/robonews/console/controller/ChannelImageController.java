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
import io.robonews.domain.ChannelImage;
import io.robonews.domain.Image;
import io.robonews.service.facebook.FacebookService;
import io.robonews.service.google.plus.GooglePlusService;
import io.robonews.service.http.HttpRequest;
import io.robonews.service.http.HttpResponse;
import io.robonews.service.http.HttpService;
import io.robonews.service.http.impl.HttpRequestImpl;
import io.robonews.service.text.tools.InputStreamReader;
import io.robonews.service.text.tools.LinkTagFetcher;
import io.robonews.service.text.tools.MetaPropertyFetcher;
import io.robonews.service.twitter.TwitterService;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
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

        return Stream.of("Facebook", "Twitter", "Google+", "Favicon", "Website")
                .parallel()
                .map(i -> getImageSource(channel, i))
                .flatMap(Collection::parallelStream)
                .map(this::getImageOption)
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

        ChannelImage image = channel.getImage() == null ? new ChannelImage() : channel.getImage();

        image.setChannel(channel);
        image.setData(imageBytes);
        image.setType(imageType);

        channel.setImage(image);

        channelDao.update(channel);

    }

    private Set<ImageSource> getImageSource(Channel channel, String source) {
        String url = null;
        Set<ImageSource> result = new HashSet<>();

        switch (source) {
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
            case "Favicon":
                if (!StringUtils.isEmpty(channel.getUrl())) {
                    url = channel.getUrl();
                    url += channel.getUrl().endsWith("/") ?  "favicon.ico" : "/favicon.ico";
                }
                break;
            case "Website":
                if (!StringUtils.isEmpty(channel.getUrl())) {
                    return crawlSiteImages(channel.getUrl());
                }
                break;
        }

        if (url != null) {
            result.add(new ImageSource(url, source));
        }

        return result;
    }

    private Set<ImageSource> crawlSiteImages(String url) {

        Set<ImageSource> result = new HashSet<>();

        HttpRequest httpRequest = new HttpRequestImpl(url);
        HttpResponse httpResponse = null;

        try {
            httpResponse = httpService.execute(httpRequest);

            if (httpResponse != null && httpResponse.getHttpStatus() == 200) {
                String content = InputStreamReader.read(httpResponse.getStream());

                Stream<Map.Entry<String, String>> metaTags = MetaPropertyFetcher
                        .fetchMetaProps(content)
                        .entrySet()
                        .stream();

                Stream<Map.Entry<String, String>> linkTags = LinkTagFetcher
                        .fetchLinkTags(content)
                        .entrySet()
                        .stream();

                return Stream
                    .concat(metaTags, linkTags)
                    .filter(e -> !StringUtils.isEmpty(e.getValue()))
                    .filter(e ->
                        "og:image".equalsIgnoreCase(e.getKey()) ||
                        "thumbnail_url".equalsIgnoreCase(e.getKey()) ||
                        "rnews:thumbnailurl".equalsIgnoreCase(e.getKey()) ||
                        "twitter:image".equalsIgnoreCase(e.getKey()) ||
                        "twitter:image:src".equalsIgnoreCase(e.getKey()) ||
                        "msapplication-square70x70logo".equalsIgnoreCase(e.getKey()) ||
                        "msapplication-square150x150logo".equalsIgnoreCase(e.getKey()) ||
                        "msapplication-square310x310logo".equalsIgnoreCase(e.getKey()) ||
                        "icon".equalsIgnoreCase(e.getKey()) ||
                        "shortcut icon".equalsIgnoreCase(e.getKey()) ||
                        "apple-touch-icon".equalsIgnoreCase(e.getKey())
                    )
                    .map(e -> {
                        String imageUrl = e.getValue();
                        if (imageUrl.startsWith("http:") || imageUrl.startsWith("https:")) {
                            return new ImageSource(imageUrl, e.getKey());
                        } else {
                            return new ImageSource(url + imageUrl, e.getKey());
                        }
                    })
                    .collect(Collectors.toSet());
            }
            else {
                return result;
            }
        }
        catch (Exception e) {
            return result;
        }
        finally {
            if (httpResponse != null) {
                httpResponse.releaseConnection();
            }
        }
    }

    private ChannelImageOption getImageOption(ImageSource imageSource) {
        HttpRequest httpRequest = new HttpRequestImpl(imageSource.getUrl());
        httpRequest.setAccept("image/png,image/*;q=0.8,*/*;q=0.5");

        HttpResponse httpResponse = null;

        try {
            httpResponse = httpService.execute(httpRequest);

            if (httpResponse != null && httpResponse.getHttpStatus() == 200
                    && httpResponse.getContentType() != null && httpResponse.getContentType().startsWith("image/")) {



                byte[] imageBytes = IOUtils.toByteArray(
                        httpResponse.getStream()
                );

                BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageBytes));

                if (image == null) {
                    return null;
                }

                ChannelImageOption imageOption = new ChannelImageOption();
                imageOption.setSource(imageSource.getSource());
                imageOption.setType(httpResponse.getContentType());
                imageOption.setWidth(image.getWidth());
                imageOption.setHeight(image.getHeight());
                imageOption.setData(Base64.encodeBase64String(imageBytes));

                return imageOption;
            }
            else {
                return null;
            }
        }
        catch (Exception e) {
            return null;
        }
        finally {
            if (httpResponse != null) {
                httpResponse.releaseConnection();
            }
        }
    }

    class ImageSource {

        private String url;

        private String source;

        public ImageSource(String url, String source) {
            this.url = url;
            this.source = source;
        }

        public String getUrl() {
            return url;
        }

        public String getSource() {
            return source;
        }
    }
}
