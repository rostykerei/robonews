/**
 * Robonews.io
 *
 * Copyright (c) 2013-2015 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.worker.image.crawler;

import io.robonews.dao.ImageCopyDao;
import io.robonews.dao.ImageDao;
import io.robonews.dao.StoryDao;
import io.robonews.domain.Image;
import io.robonews.domain.ImageCopy;
import io.robonews.domain.Story;
import io.robonews.domain.StoryImage;
import io.robonews.messaging.domain.ImageMessage;
import io.robonews.service.http.HttpRequest;
import io.robonews.service.http.HttpResponse;
import io.robonews.service.http.HttpService;
import io.robonews.service.http.impl.HttpRequestImpl;
import io.robonews.service.image.tools.ImageHash;
import io.robonews.service.image.tools.ImageSave;
import io.robonews.service.image.tools.ImageScale;
import io.robonews.service.storage.StorageService;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class ImageQueueListener {

    @Autowired
    private StoryDao storyDao;

    @Autowired
    private ImageDao imageDao;

    @Autowired
    private ImageCopyDao imageCopyDao;

    @Autowired
    private HttpService httpService;

    @Autowired
    private StorageService storageService;

    @Value("#{'${workerImageCrawler.imageSizes}'.split(',')}")
    private List<Integer> imageSizes;

    private Logger logger = LoggerFactory.getLogger(ImageQueueListener.class);

    private static final float JPEG_COMPRESSION_RATE = 0.85F;

    private static final Format STORAGE_DIR_FORMAT = new SimpleDateFormat("yyMMdd");

    public void listen(ImageMessage message) {
        try {
            processMessage(message);
        }
        catch (java.io.IOException e) {
            logger.info("Failed to process image: " + message.getImageUrl() + " error: " + e.getMessage());
        }
        catch (Exception e) {
            logger.info("Failed to process image", e);
        }
    }

    private void processMessage(ImageMessage message) throws IOException {
        Story story = storyDao.getByIdWithTags(message.getStoryId());

        if (story == null) {
            logger.info("Story not found");
            return;
        }

        if (message.getImageUrl() == null || message.getImageUrl().isEmpty()) {
            logger.info("Image URL is empty");
            return;
        }

        if (message.getImageUrl().length() > 255) {
            logger.info("Image URL is too long");
            return;
        }

        Image existentImage = imageDao.getByUrl(message.getImageUrl());

        if (existentImage != null) {
            try {
                storyDao.saveStoryImage(new StoryImage(story, existentImage));
            }
            catch (DataIntegrityViolationException e) {
                // do nothing, already linked...
            }

            return;
        }

        File tempImageFile = crawlImage(message.getImageUrl());

        try {
            BufferedImage imageFile = ImageIO.read(tempImageFile);

            if (imageFile == null) {
                logger.info("Failed to load image from temp file");
                return;
            }

            long imgSize = tempImageFile.length();
            long imgCrc32 = FileUtils.checksumCRC32(tempImageFile);

            Image image = new Image();
            image.setSourceChannel(story.getChannel());
            image.setSourceStory(story);
            image.setType(determinateImageType(tempImageFile));
            image.setUrl(message.getImageUrl());
            image.setSize(imgSize);
            image.setWidth(imageFile.getWidth());
            image.setHeight(imageFile.getHeight());

            image.setCrcHash(imgCrc32);
            image.setpHash(ImageHash.calculatePHash(imageFile));

            try {
                imageDao.create(image);
            }
            catch (DataIntegrityViolationException e) {
                // Trying if image is already exists
                image = imageDao.getByIndex(story.getChannel().getId(), imgSize, imgCrc32);

                if (image == null) {
                    throw e;
                }
            }

            try {
                storyDao.saveStoryImage(new StoryImage(story, image));
            }
            catch (DataIntegrityViolationException e) {
                // Do nothing, already linked...
            }


            if (image.getRatio() < 0.4167 || image.getRatio() > 2.4) {
                // Very un-proportional image
                return;
            }

            for (Integer size : imageSizes) {
                if (image.getWidth() >= size) {
                    resizeAndUpload(image, imageFile, size);
                }
            }
        }
        finally {
            tempImageFile.delete();
        }
    }

    private File crawlImage(String url) throws IOException {
        HttpRequest httpRequest = new HttpRequestImpl(url);
        httpRequest.setAccept("image/jpeg,image/png,image/gif,image/*");

        HttpResponse httpResponse = null;

        try {
            httpResponse = httpService.execute(httpRequest);

            if (httpResponse.getHttpStatus() != HttpResponse.STATUS_OK) {
                throw new IOException("HTTP code [" + httpResponse.getHttpStatus() + "] returned");
            }

            File tempFile = File.createTempFile("image-crawler-", ".img");
            tempFile.deleteOnExit();

            try {
                IOUtils.copy(httpResponse.getStream(), new FileOutputStream(tempFile));
            }
            catch (Exception e) {
                tempFile.delete();
                throw new IOException(e);
            }

            return tempFile;
        }
        finally {
            if (httpResponse != null) {
                httpResponse.releaseConnection();
            }
        }
    }

    private void resizeAndUpload(Image image, BufferedImage bufferedImage, int newWidth) {
        BufferedImage resizedImage = ImageScale.resize(bufferedImage, newWidth);

        File resizedImageFile;
        String fileExtension;
        Image.Type fileType;

        try {
            if (image.getType() == Image.Type.PNG) {
                resizedImageFile = ImageSave.saveTempPng(resizedImage);
                fileType = Image.Type.PNG;
                fileExtension = ".png";
            }
            else {
                resizedImageFile = ImageSave.saveTempJpeg(resizedImage, JPEG_COMPRESSION_RATE);
                fileType = Image.Type.JPEG;
                fileExtension = ".jpg";
            }
        }
        catch (IOException e) {
            logger.error("Could not save resized image", e);
            return;
        }

        Date creationDate = new Date();

        ImageCopy imageCopy = new ImageCopy();
        imageCopy.setImage(image);
        imageCopy.setType(fileType);
        imageCopy.setWidth(resizedImage.getWidth());
        imageCopy.setHeight(resizedImage.getHeight());
        imageCopy.setSize(resizedImageFile.length());
        imageCopy.setCreatedDate(creationDate);
        imageCopy.setDeleteAfterDate(calculateDeleteAfterDate(creationDate));
        imageCopy.setDirectory(STORAGE_DIR_FORMAT.format(creationDate));

        try {
            storageService.putFile(
                resizedImageFile,
                imageCopy.getUid() + fileExtension,
                imageCopy.getDirectory(),
                getContentType(fileType),
                imageCopy.getDeleteAfterDate()
            );

            imageCopyDao.create(imageCopy);

        } catch (IOException e) {
            logger.error("Could not store imageCopy", e);
            return;
        } catch (Exception e) {
            logger.error("Could not save imageCopy", e);
            return;
        }
        finally {
            try {
                resizedImageFile.delete();
            }
            catch (Exception e) {
                // ignore..
            }
        }
    }

    private Date calculateDeleteAfterDate(Date creationDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(creationDate);
        cal.add(Calendar.MONTH, 1);
        return cal.getTime();
    }

    private String getContentType(Image.Type imageType) {
        switch (imageType) {
            case JPEG:
                return "image/jpeg";
            case PNG:
                return "image/png";
            case GIF:
                return "image/gif";
            default:
                return "";
        }
    }

    private Image.Type determinateImageType(File imageFile) throws IOException {
        ImageInputStream iis = ImageIO.createImageInputStream(imageFile);

        Iterator<ImageReader> readers = ImageIO.getImageReaders(iis);

        if (readers != null && readers.hasNext()) {
            ImageReader read = readers.next();

            String name = read.getFormatName();

            if ("JPEG".equalsIgnoreCase(name) || "JPG".equalsIgnoreCase(name)) {
                return Image.Type.JPEG;
            }
            else if ("PNG".equalsIgnoreCase(name)) {
                return Image.Type.PNG;
            }
            else if ("GIF".equalsIgnoreCase(name)) {
                return Image.Type.GIF;
            }
            else {
                throw new IOException("Unknown format [" + name + "]");
            }
        }
        else {
            throw new IOException("Not image file");
        }

    }
}
