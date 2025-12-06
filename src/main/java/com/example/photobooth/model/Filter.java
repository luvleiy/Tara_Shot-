package com.example.photobooth.model;

import java.awt.image.BufferedImage;

public interface Filter {
    BufferedImage apply(BufferedImage image);
    String getName();
}
