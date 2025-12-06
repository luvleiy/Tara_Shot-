package com.example.photobooth.model;

import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

public class RotateFilter implements Filter {
    @Override
    public BufferedImage apply(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage rotated = new BufferedImage(height, width, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = rotated.createGraphics();
        AffineTransform at = new AffineTransform();
        at.translate(height, 0);
        at.rotate(Math.PI / 2);
        g2d.drawImage(image, at, null);
        g2d.dispose();
        return rotated;
    }

    @Override
    public String getName() {
        return "Rotate";
    }
}
