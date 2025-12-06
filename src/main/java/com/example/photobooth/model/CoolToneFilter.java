package com.example.photobooth.model;

import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

public class CoolToneFilter implements Filter {

    @Override
    public BufferedImage apply(BufferedImage image) {
       
        BufferedImage mirrored = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = mirrored.createGraphics();
        AffineTransform at = AffineTransform.getScaleInstance(-1, 1);
        at.translate(-image.getWidth(), 0);
        g2d.drawImage(image, at, null);
        g2d.dispose();

        
        BufferedImage coolImage = new BufferedImage(mirrored.getWidth(), mirrored.getHeight(), BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < mirrored.getHeight(); y++) {
            for (int x = 0; x < mirrored.getWidth(); x++) {
                int rgb = mirrored.getRGB(x, y);
                int r = (rgb >> 16) & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int b = rgb & 0xFF;

                
                int newR = (int) Math.min(255, Math.max(0, r * 0.8));
                int newG = (int) Math.min(255, Math.max(0, g * 0.9));
                int newB = (int) Math.min(255, Math.max(0, b * 1.2));

                int newRgb = (newR << 16) | (newG << 8) | newB;
                coolImage.setRGB(x, y, newRgb);
            }
        }
        return coolImage;
    }

    @Override
    public String getName() {
        return "Cool Tone";
    }
}
