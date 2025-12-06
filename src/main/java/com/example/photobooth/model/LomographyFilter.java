package com.example.photobooth.model;

import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

public class LomographyFilter implements Filter {

    @Override
    public BufferedImage apply(BufferedImage image) {
        
        BufferedImage mirrored = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = mirrored.createGraphics();
        AffineTransform at = AffineTransform.getScaleInstance(-1, 1);
        at.translate(-image.getWidth(), 0);
        g2d.drawImage(image, at, null);
        g2d.dispose();

       
        BufferedImage lomoImage = new BufferedImage(mirrored.getWidth(), mirrored.getHeight(), BufferedImage.TYPE_INT_RGB);
        int centerX = mirrored.getWidth() / 2;
        int centerY = mirrored.getHeight() / 2;
        double maxDist = Math.sqrt(centerX * centerX + centerY * centerY);

        for (int y = 0; y < mirrored.getHeight(); y++) {
            for (int x = 0; x < mirrored.getWidth(); x++) {
                int rgb = mirrored.getRGB(x, y);
                int r = (rgb >> 16) & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int b = rgb & 0xFF;

                int newR = (int) Math.min(255, Math.max(0, 255 - r * 0.8 + g * 0.2));
                int newG = (int) Math.min(255, Math.max(0, g * 1.2));
                int newB = (int) Math.min(255, Math.max(0, b * 1.1 + r * 0.1));

              
                double dist = Math.sqrt((x - centerX) * (x - centerX) + (y - centerY) * (y - centerY));
                double vignette = 1 - (dist / maxDist) * 0.3;
                newR = (int) (newR * vignette);
                newG = (int) (newG * vignette);
                newB = (int) (newB * vignette);

                int newRgb = (newR << 16) | (newG << 8) | newB;
                lomoImage.setRGB(x, y, newRgb);
            }
        }
        return lomoImage;
    }

    @Override
    public String getName() {
        return "Lomography";
    }
}
