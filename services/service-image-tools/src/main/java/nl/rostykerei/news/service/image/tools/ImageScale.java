/**
 * Robonews.io
 *
 * Copyright (c) 2013-2014 Rosty Kerei.
 * All rights reserved.
 */
package nl.rostykerei.news.service.image.tools;

import java.awt.image.BufferedImage;
import org.imgscalr.Scalr;

public class ImageScale {

    public static BufferedImage resize(BufferedImage source, int targetWidth) {
        return Scalr.resize(source, Scalr.Method.ULTRA_QUALITY, Scalr.Mode.FIT_TO_WIDTH, targetWidth);
    }

}
