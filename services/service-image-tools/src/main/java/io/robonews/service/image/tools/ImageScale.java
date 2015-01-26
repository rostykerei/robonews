/**
 * Robonews.io
 *
 * Copyright (c) 2013-2015 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.service.image.tools;

import org.imgscalr.Scalr;

import java.awt.image.BufferedImage;

public class ImageScale {

    public static BufferedImage resize(BufferedImage source, int targetWidth) {
        return Scalr.resize(source, Scalr.Method.ULTRA_QUALITY, Scalr.Mode.FIT_TO_WIDTH, targetWidth);
    }

}
