/**
 * Robonews.io
 *
 * Copyright (c) 2013-2015 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.service.image.tools;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Random;

/**
 * Created by rosty on 27/04/15.
 */
public class ImageAvatarGenerator {

    public static byte[] generateAvatar(String name) {
        BufferedImage bufferedImage = new BufferedImage(48, 48, BufferedImage.TYPE_INT_RGB);
        Graphics graphics = bufferedImage.getGraphics();
        graphics.setColor(new Color((int) (Math.random() * 148 + 64),
                (int) (Math.random() * 148 + 64),
                (int) (Math.random() * 148 + 64)));

        graphics.fillRect(0, 0, 48, 48);
        graphics.setColor(Color.WHITE);

        String s = name.substring(0, 1).toUpperCase();

        graphics.setFont(new Font("Arial", Font.PLAIN, 32));

        FontMetrics fm = graphics.getFontMetrics();
        int w = fm.stringWidth(s);

        graphics.drawString(s, Math.round((48 - w) / 2) + 1, 35);


        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {
            ImageIO.write(bufferedImage, "png", baos);
        } catch (IOException e) {
            e.printStackTrace();
            // TODO log
        }

        return baos.toByteArray();
    }
}
