package com.example.photobooth.model;

import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.Random;

public class FilmGrainFilter implements Filter {

    private Random random = new Random();

    @Override
    public BufferedImage apply(BufferedImage image) {
       
        BufferedImage mirrored = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = mirrored.createGraphics();
        AffineTransform at = AffineTransform.getScaleInstance(-1, 1);
        at.translate(-image.getWidth(), 0);
        g2d.drawImage(image, at, null);
        g2d.dispose();

        
        BufferedImage grainImage = new BufferedImage(mirrored.getWidth(), mirrored.getHeight(), BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < mirrored.getHeight(); y++) {
            for (int x = 0; x < mirrored.getWidth(); x++) {
                int rgb = mirrored.getRGB(x, y);
                int r = (rgb >> 16) & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int b = rgb & 0xFF;

        
                int grain = random.nextInt(51) - 25; // -25 to 25
                int newR = Math.min(255, Math.max(0, r + grain));
                int newG = Math.min(255, Math.max(0, g + grain));
                int newB = Math.min(255, Math.max(0, b + grain));

                int newRgb = (newR << 16) | (newG << 8) | newB;
                grainImage.setRGB(x, y, newRgb);
            }
        }
        return grainImage;
    }

    @Override
    public String getName() {
        return "Film Grain";
    }
}
