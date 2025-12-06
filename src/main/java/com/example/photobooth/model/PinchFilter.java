package com.example.photobooth.model;

import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

public class PinchFilter implements Filter {

    @Override
    public BufferedImage apply(BufferedImage image) {
        
        BufferedImage pinchImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        int centerX = image.getWidth() / 2;
        int centerY = image.getHeight() / 2;
        double radius = Math.min(centerX, centerY) * 0.8;

        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                double dx = x - centerX;
                double dy = y - centerY;
                double dist = Math.sqrt(dx * dx + dy * dy);

                if (dist < radius) {
                    double factor = dist / radius;
                    double pinch = 1 - 0.5 * Math.sin(factor * Math.PI);
                    int srcX = (int) (centerX + dx * pinch);
                    int srcY = (int) (centerY + dy * pinch);

                    if (srcX >= 0 && srcX < image.getWidth() && srcY >= 0 && srcY < image.getHeight()) {
                        pinchImage.setRGB(x, y, image.getRGB(srcX, srcY));
                    } else {
                        pinchImage.setRGB(x, y, 0); 
                    }
                } else {
                    pinchImage.setRGB(x, y, image.getRGB(x, y));
                }
            }
        }
        return pinchImage;
    }

    @Override
    public String getName() {
        return "Pinch";
    }
}
