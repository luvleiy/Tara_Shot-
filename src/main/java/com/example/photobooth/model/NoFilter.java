package com.example.photobooth.model;

import java.awt.image.BufferedImage;

public class NoFilter implements Filter {
    @Override
    public BufferedImage apply(BufferedImage image) {
        
        return image;
    }

    @Override
    public String getName() {
        return "No Filter";
    }
}
