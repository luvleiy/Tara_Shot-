package com.example.photobooth.model;

import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

public class ZoomFilter implements Filter {
    @Override
    public BufferedImage apply(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage zoomed = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = zoomed.createGraphics();
        AffineTransform at = new AffineTransform();
        at.scale(1.2, 1.2);
        at.translate(-width * 0.1, -height * 0.1);
        g2d.drawImage(image, at, null);
        g2d.dispose();
        return zoomed;
    }

    @Override
    public String getName() {
        return "Zoom";
    }
}
