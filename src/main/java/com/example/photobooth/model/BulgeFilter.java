package com.example.photobooth.model;

import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

public class BulgeFilter implements Filter {

    @Override
    public BufferedImage apply(BufferedImage image) {
     
        BufferedImage bulgeImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
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
                    double bulge = 1 + 0.5 * Math.sin(factor * Math.PI);
                    int srcX = (int) (centerX + dx * bulge);
                    int srcY = (int) (centerY + dy * bulge);

                    if (srcX >= 0 && srcX < image.getWidth() && srcY >= 0 && srcY < image.getHeight()) {
                        bulgeImage.setRGB(x, y, image.getRGB(srcX, srcY));
                    } else {
                        bulgeImage.setRGB(x, y, 0); 
                    }
                } else {
                    bulgeImage.setRGB(x, y, image.getRGB(x, y));
                }
            }
        }
        return bulgeImage;
    }

    @Override
    public String getName() {
        return "Bulge";
    }
}
