package com.example.photobooth.model;

import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

public class SwirlFilter implements Filter {

    @Override
    public BufferedImage apply(BufferedImage image) {
        BufferedImage swirlImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        int centerX = image.getWidth() / 2;
        int centerY = image.getHeight() / 2;
        double radius = Math.min(centerX, centerY) * 0.8;
        double angle = 2 * Math.PI;

        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                double dx = x - centerX;
                double dy = y - centerY;
                double dist = Math.sqrt(dx * dx + dy * dy);

                if (dist < radius) {
                    double factor = 1 - dist / radius;
                    double swirlAngle = angle * factor * factor;
                    double cos = Math.cos(swirlAngle);
                    double sin = Math.sin(swirlAngle);
                    int srcX = (int) (centerX + dx * cos - dy * sin);
                    int srcY = (int) (centerY + dx * sin + dy * cos);

                    if (srcX >= 0 && srcX < image.getWidth() && srcY >= 0 && srcY < image.getHeight()) {
                        swirlImage.setRGB(x, y, image.getRGB(srcX, srcY));
                    } else {
                        swirlImage.setRGB(x, y, 0); 
                    }
                } else {
                    swirlImage.setRGB(x, y, image.getRGB(x, y));
                }
            }
        }
        return swirlImage;
    }

    @Override
    public String getName() {
        return "Swirl";
    }
}
