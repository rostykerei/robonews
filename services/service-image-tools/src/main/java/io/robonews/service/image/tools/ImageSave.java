/**
 * Robonews.io
 *
 * Copyright (c) 2013-2014 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.service.image.tools;

import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

public class ImageSave {

    public static File saveTempJpeg(RenderedImage image, float quality) throws IOException {
        ImageWriter writer = ImageIO.getImageWritersByFormatName("jpeg").next();

        ImageWriteParam param = writer.getDefaultWriteParam();
        param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        param.setCompressionQuality(quality);

        File tempFile = File.createTempFile("temp-jpeg-img-", ".jpg");
        tempFile.deleteOnExit();

        ImageOutputStream imageOutputStream = ImageIO.createImageOutputStream(tempFile);

        writer.setOutput(imageOutputStream);
        writer.write(null, new IIOImage(image, null, null), param);

        writer.dispose();

        imageOutputStream.close();

        return tempFile;
    }

    public static File saveTempPng(RenderedImage image) throws IOException {
        ImageWriter writer = ImageIO.getImageWritersByFormatName("png").next();

        ImageWriteParam param = writer.getDefaultWriteParam();

        File tempFile = File.createTempFile("temp-png-img-", ".png");
        tempFile.deleteOnExit();

        ImageOutputStream imageOutputStream = ImageIO.createImageOutputStream(tempFile);

        writer.setOutput(imageOutputStream);
        writer.write(null, new IIOImage(image, null, null), param);

        writer.dispose();

        imageOutputStream.close();

        return tempFile;
    }

}
