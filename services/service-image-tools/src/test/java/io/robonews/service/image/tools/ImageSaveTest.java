/**
 * Robonews.io
 *
 * Copyright (c) 2013-2014 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.service.image.tools;

import org.junit.Assert;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;


public class ImageSaveTest {

    @Test
    public void testSaveTempJpeg() throws Exception {
        BufferedImage original = ImageIO.read(getClass().getResource("/Lenna.jpg"));

        File full = ImageSave.saveTempJpeg(original, 1F);
        File loss = ImageSave.saveTempJpeg(original, 0.01F);

        Assert.assertNotNull(full);
        Assert.assertNotNull(loss);

        Assert.assertTrue(full.exists());
        Assert.assertTrue(full.isFile());
        Assert.assertTrue(full.canRead());

        Assert.assertTrue(loss.exists());
        Assert.assertTrue(loss.isFile());
        Assert.assertTrue(loss.canRead());

        Assert.assertTrue(full.length() > loss.length());

        BufferedImage fullImg = ImageIO.read(full);
        BufferedImage lossImg = ImageIO.read(loss);

        Assert.assertTrue(original.getWidth() == fullImg.getWidth());
        Assert.assertTrue(lossImg.getHeight() == fullImg.getHeight());

        full.delete();
        loss.delete();
    }

    @Test
    public void testSaveTempPng() throws Exception {
        BufferedImage original = ImageIO.read(getClass().getResource("/Lenna.jpg"));

        File pngFile = ImageSave.saveTempPng(original);

        Assert.assertNotNull(pngFile);

        Assert.assertTrue(pngFile.exists());
        Assert.assertTrue(pngFile.isFile());
        Assert.assertTrue(pngFile.canRead());

        BufferedImage fullImg = ImageIO.read(pngFile);

        Assert.assertTrue(original.getWidth() == fullImg.getWidth());
        Assert.assertTrue(fullImg.getHeight() == fullImg.getHeight());

        pngFile.delete();
    }
}
