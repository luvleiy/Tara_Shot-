package com.example.photobooth.model;

import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.Color;

public class ShearFilter implements Filter {
    @Override
    public BufferedImage apply(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage sheared = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = sheared.createGraphics();
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, width, height);
        AffineTransform at = new AffineTransform();
        at.shear(0.2, 0);
        g2d.drawImage(image, at, null);
        g2d.dispose();
        return sheared;
    }

    @Override
    public String getName() {
        return "Shear";
    }
}
