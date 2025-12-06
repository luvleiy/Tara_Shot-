package com.example.photobooth.model;

import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

public class HighContrastFilter implements Filter {

    @Override
    public BufferedImage apply(BufferedImage image) {
      
        BufferedImage mirrored = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = mirrored.createGraphics();
        AffineTransform at = AffineTransform.getScaleInstance(-1, 1);
        at.translate(-image.getWidth(), 0);
        g2d.drawImage(image, at, null);
        g2d.dispose();

        
        BufferedImage contrastImage = new BufferedImage(mirrored.getWidth(), mirrored.getHeight(), BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < mirrored.getHeight(); y++) {
            for (int x = 0; x < mirrored.getWidth(); x++) {
                int rgb = mirrored.getRGB(x, y);
                int r = (rgb >> 16) & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int b = rgb & 0xFF;

                int newR = r > 128 ? Math.min(255, r + 50) : Math.max(0, r - 50);
                int newG = g > 128 ? Math.min(255, g + 50) : Math.max(0, g - 50);
                int newB = b > 128 ? Math.min(255, b + 50) : Math.max(0, b - 50);

                int newRgb = (newR << 16) | (newG << 8) | newB;
                contrastImage.setRGB(x, y, newRgb);
            }
        }
        return contrastImage;
    }

    @Override
    public String getName() {
        return "High Contrast";
    }
}
