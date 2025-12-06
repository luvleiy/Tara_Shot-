package com.example.photobooth.model;

import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

public class PolaroidFilter implements Filter {

    @Override
    public BufferedImage apply(BufferedImage image) {
      
        BufferedImage mirrored = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = mirrored.createGraphics();
        AffineTransform at = AffineTransform.getScaleInstance(-1, 1);
        at.translate(-image.getWidth(), 0);
        g2d.drawImage(image, at, null);
        g2d.dispose();

       
        BufferedImage polaroidImage = new BufferedImage(mirrored.getWidth(), mirrored.getHeight(), BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < mirrored.getHeight(); y++) {
            for (int x = 0; x < mirrored.getWidth(); x++) {
                int rgb = mirrored.getRGB(x, y);
                int r = (rgb >> 16) & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int b = rgb & 0xFF;

                int newR = (int) Math.min(255, Math.max(0, (r * 1.1) + (g * 0.05) + (b * 0.05) + 10));
                int newG = (int) Math.min(255, Math.max(0, (r * 0.05) + (g * 1.05) + (b * 0.05) + 5));
                int newB = (int) Math.min(255, Math.max(0, (r * 0.05) + (g * 0.05) + (b * 0.9) - 5));

                int newRgb = (newR << 16) | (newG << 8) | newB;
                polaroidImage.setRGB(x, y, newRgb);
            }
        }
        return polaroidImage;
    }

    @Override
    public String getName() {
        return "Polaroid";
    }
}
