package nl.rostykerei.news.service.image.tools;

import java.awt.image.BufferedImage;
import org.imgscalr.Scalr;

public class ImageScale {

    public static BufferedImage resize(BufferedImage source, int targetWidth) {
        return Scalr.resize(source, Scalr.Method.ULTRA_QUALITY, Scalr.Mode.FIT_TO_WIDTH, targetWidth);
    }

}
