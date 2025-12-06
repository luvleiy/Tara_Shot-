package com.example.photobooth.model;

import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

public class FlipFilter implements Filter {
    @Override
    public BufferedImage apply(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage flipped = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = flipped.createGraphics();
        AffineTransform at = new AffineTransform();
        at.scale(1, -1);
        at.translate(0, -height);
        g2d.drawImage(image, at, null);
        g2d.dispose();
        return flipped;
    }

    @Override
    public String getName() {
        return "Flip";
    }
}
