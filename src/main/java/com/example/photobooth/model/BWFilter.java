package com.example.photobooth.model;

import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.geom.AffineTransform;

public class BWFilter implements Filter {

    @Override
    public BufferedImage apply(BufferedImage image) {
       
        BufferedImage mirrored = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = mirrored.createGraphics();
        AffineTransform at = AffineTransform.getScaleInstance(-1, 1);
        at.translate(-image.getWidth(), 0);
        g2d.drawImage(image, at, null);
        g2d.dispose();

       
        BufferedImage bwImage = new BufferedImage(mirrored.getWidth(), mirrored.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
        g2d = bwImage.createGraphics();
        g2d.drawImage(mirrored, 0, 0, null);
        g2d.dispose();

       
        return adjustContrastBrightness(bwImage, 1.5f, -50);
    }

    @Override
    public String getName() {
        return "Retro B&W";
    }

    private BufferedImage adjustContrastBrightness(BufferedImage image, float contrast, int brightness) {
        BufferedImage result = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int rgb = image.getRGB(x, y);
                int r = (rgb >> 16) & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int b = rgb & 0xFF;

               
                r = (int) Math.min(255, Math.max(0, (r - 128) * contrast + 128 + brightness));
                g = (int) Math.min(255, Math.max(0, (g - 128) * contrast + 128 + brightness));
                b = (int) Math.min(255, Math.max(0, (b - 128) * contrast + 128 + brightness));

                int newRgb = (r << 16) | (g << 8) | b;
                result.setRGB(x, y, newRgb);
            }
        }
        return result;
    }
}
