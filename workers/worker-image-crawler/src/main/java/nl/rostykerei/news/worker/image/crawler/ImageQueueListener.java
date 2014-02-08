package nl.rostykerei.news.worker.image.crawler;

import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import nl.rostykerei.news.dao.ImageDao;
import nl.rostykerei.news.dao.StoryDao;
import nl.rostykerei.news.domain.Image;
import nl.rostykerei.news.domain.Story;
import nl.rostykerei.news.domain.StoryImage;
import nl.rostykerei.news.messaging.domain.ImageMessage;
import nl.rostykerei.news.service.http.HttpRequest;
import nl.rostykerei.news.service.http.HttpResponse;
import nl.rostykerei.news.service.http.HttpService;
import nl.rostykerei.news.service.http.impl.HttpRequestImpl;
import nl.rostykerei.news.service.image.tools.ImageHash;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

public class ImageQueueListener {

    @Autowired
    private StoryDao storyDao;

    @Autowired
    private ImageDao imageDao;

    @Autowired
    private HttpService httpService;

    private Logger logger = LoggerFactory.getLogger(ImageQueueListener.class);

    private static ColorConvertOp colorConvert = new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_GRAY), null);

    public void listen(ImageMessage message) {
        try {
            processMessage(message);
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

        Image existentImage = imageDao.getByUrl(message.getImageUrl());

        if (existentImage != null) {
            storyDao.saveStoryImage(new StoryImage(story, existentImage));
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

            storyDao.saveStoryImage(new StoryImage(story, image));
        }
        finally {
            tempImageFile.delete();
        }
    }

    private File crawlImage(String url) throws IOException {
        HttpRequest httpRequest = new HttpRequestImpl(url);
        httpRequest.setAccept("image/jpeg,image/png,image/gif,image/*");

        HttpResponse httpResponse = httpService.execute(httpRequest);

        if (httpResponse == null) {
            throw new IOException("HTTP service returned null response");
        }

        if (httpResponse.getHttpStatus() != 200) {
            throw new IOException("HTTP code [" + httpResponse.getHttpStatus() + "] returned");
        }

        File tempFile = File.createTempFile("image-crawler-", ".img");
        tempFile.deleteOnExit();

        try {
            IOUtils.copy(httpResponse.getStream(), new FileOutputStream(tempFile));

            if (httpResponse != null) {
                httpResponse.releaseConnection();
            }
        }
        catch (Exception e) {
            tempFile.delete();
            throw new IOException(e);
        }

        return tempFile;
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
        }

        throw new IOException("Unknown format");
    }
}
