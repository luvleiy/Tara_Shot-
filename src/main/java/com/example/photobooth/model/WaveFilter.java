package com.example.photobooth.model;

import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

public class WaveFilter implements Filter {

    @Override
    public BufferedImage apply(BufferedImage image) {
        
        BufferedImage waveImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        double amplitude = 10.0;
        double frequency = 0.02;

        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int offsetX = (int) (amplitude * Math.sin(y * frequency));
                int srcX = Math.max(0, Math.min(image.getWidth() - 1, x + offsetX));
                waveImage.setRGB(x, y, image.getRGB(srcX, y));
            }
        }
        return waveImage;
    }

    @Override
    public String getName() {
        return "Wave";
    }
}
