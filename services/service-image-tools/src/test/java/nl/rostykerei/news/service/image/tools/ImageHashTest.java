/**
 * Robonews.io
 *
 * Copyright (c) 2013-2014 Rosty Kerei.
 * All rights reserved.
 */
package nl.rostykerei.news.service.image.tools;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import org.junit.Assert;
import org.junit.Test;

public class ImageHashTest {

    @Test
    public void testCalculatePHash() throws Exception{
        BufferedImage original = ImageIO.read(getClass().getResource("/Lenna.jpg"));
        BufferedImage smaller = ImageIO.read(getClass().getResource("/Lenna-smaller.jpg"));
        BufferedImage grayscale = ImageIO.read(getClass().getResource("/Lenna-grayscale.jpg"));
        BufferedImage contrast = ImageIO.read(getClass().getResource("/Lenna-contrast.jpg"));
        BufferedImage monaLisa = ImageIO.read(getClass().getResource("/Mona-Lisa.jpg"));

        byte[] originalHash = ImageHash.calculatePHash(original);
        byte[] smallerHash = ImageHash.calculatePHash(smaller);
        byte[] grayscaleHash = ImageHash.calculatePHash(grayscale);
        byte[] contrastHash = ImageHash.calculatePHash(contrast);
        byte[] monaLisaHash = ImageHash.calculatePHash(monaLisa);

        Assert.assertTrue(ImageHash.hammingDistance(originalHash, smallerHash) < 8);
        Assert.assertTrue(ImageHash.hammingDistance(originalHash, grayscaleHash) < 8);
        Assert.assertTrue(ImageHash.hammingDistance(originalHash, contrastHash) < 8);
        Assert.assertTrue(ImageHash.hammingDistance(grayscaleHash, contrastHash) < 8);
        Assert.assertTrue(ImageHash.hammingDistance(contrastHash, smallerHash) < 8);

        Assert.assertTrue(ImageHash.hammingDistance(originalHash, monaLisaHash) > 16);
        Assert.assertTrue(ImageHash.hammingDistance(monaLisaHash, contrastHash) > 16);
    }
}
