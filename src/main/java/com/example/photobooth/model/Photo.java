package com.example.photobooth.model;

import java.awt.image.BufferedImage;
import java.util.List;

public class Photo {
    private BufferedImage image;

    public Photo(BufferedImage image) {
        this.image = image;
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    public void applyFilter(Filter filter) {
        this.image = filter.apply(this.image);
    }

    public void applyFilters(List<Filter> filters) {
        for (Filter filter : filters) {
            this.image = filter.apply(this.image);
        }
    }
}
