package com.example.photobooth.model;

import java.awt.image.BufferedImage;

public class StretchFilter implements Filter {

    @Override
    public BufferedImage apply(BufferedImage image) {
        BufferedImage stretchImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        int centerY = image.getHeight() / 2;
        int stretchHeight = image.getHeight() / 3;

        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int srcX = x;
                int srcY = y;

                if (Math.abs(y - centerY) < stretchHeight) {
                    double factor = 1.0 - Math.abs(y - centerY) / (double) stretchHeight;
                    double stretchFactor = 1 + factor * 1.0;
                    srcY = (int) (centerY + (y - centerY) / stretchFactor);
                }

                if (srcX >= 0 && srcX < image.getWidth() && srcY >= 0 && srcY < image.getHeight()) {
                    stretchImage.setRGB(x, y, image.getRGB(srcX, srcY));
                } else {
                    stretchImage.setRGB(x, y, 0);
                }
            }
        }
        return stretchImage;
    }

    @Override
    public String getName() {
        return "Stretch";
    }
}
