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

public class ImageScaleTest {

    @Test
    public void testResize() throws Exception {
        BufferedImage original = ImageIO.read(getClass().getResource("/Lenna.jpg"));
        BufferedImage resized = ImageScale.resize(original, 256);

        Assert.assertEquals(256, resized.getHeight());
        Assert.assertEquals(256, resized.getWidth());

        BufferedImage monaLisa = ImageIO.read(getClass().getResource("/Mona-Lisa.jpg"));
        BufferedImage resizedMonaLisa = ImageScale.resize(monaLisa, 256);

        Assert.assertEquals(381, resizedMonaLisa.getHeight());
        Assert.assertEquals(256, resizedMonaLisa.getWidth());
    }
}
