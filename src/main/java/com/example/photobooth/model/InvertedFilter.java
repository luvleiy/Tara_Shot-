package com.example.photobooth.model;

import java.awt.image.BufferedImage;

public class InvertedFilter implements Filter {
    private final Filter baseFilter;
    private final MirrorFilter mirrorFilter = new MirrorFilter();

    public InvertedFilter(Filter baseFilter) {
        this.baseFilter = baseFilter;
    }

    @Override
    public BufferedImage apply(BufferedImage image) {
        BufferedImage filteredImage = baseFilter.apply(image);
        return mirrorFilter.apply(filteredImage);
    }

    @Override
    public String getName() {
        return baseFilter.getName() + " (Inverted)";
    }
}
