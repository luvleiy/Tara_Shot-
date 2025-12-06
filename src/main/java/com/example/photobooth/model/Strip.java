package com.example.photobooth.model;

import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.util.List;

public class Strip {
    private List<Photo> photos;
    private Layout layout;

    public enum Layout {
        SINGLE, VERTICAL_3, GRID_2x2, HORIZONTAL_2, VERTICAL_4, GRID_3x3
    }

    public Strip(List<Photo> photos, Layout layout) {
        this.photos = photos;
        this.layout = layout;
    }

    public BufferedImage generateStrip() {
        if (photos.isEmpty()) return null;
        BufferedImage strip;
        Graphics2D g2d;
        int width = photos.get(0).getImage().getWidth();
        int height = photos.get(0).getImage().getHeight();

        switch (layout) {
            case SINGLE:
                strip = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                g2d = strip.createGraphics();
                if (!photos.isEmpty()) {
                    g2d.drawImage(photos.get(0).getImage(), 0, 0, null);
                }
                break;
            case VERTICAL_3:
                strip = new BufferedImage(width, height * 3, BufferedImage.TYPE_INT_RGB);
                g2d = strip.createGraphics();
                for (int i = 0; i < photos.size(); i++) {
                    g2d.drawImage(photos.get(i).getImage(), 0, i * height, null);
                }
                break;
            case GRID_2x2:
                strip = new BufferedImage(width * 2, height * 2, BufferedImage.TYPE_INT_RGB);
                g2d = strip.createGraphics();
                for (int i = 0; i < photos.size() && i < 4; i++) {
                    int x = (i % 2) * width;
                    int y = (i / 2) * height;
                    g2d.drawImage(photos.get(i).getImage(), x, y, null);
                }
                break;
            case HORIZONTAL_2:
                strip = new BufferedImage(width * 2, height, BufferedImage.TYPE_INT_RGB);
                g2d = strip.createGraphics();
                for (int i = 0; i < photos.size() && i < 2; i++) {
                    g2d.drawImage(photos.get(i).getImage(), i * width, 0, null);
                }
                break;
            case VERTICAL_4:
                strip = new BufferedImage(width, height * 4, BufferedImage.TYPE_INT_RGB);
                g2d = strip.createGraphics();
                for (int i = 0; i < photos.size(); i++) {
                    g2d.drawImage(photos.get(i).getImage(), 0, i * height, null);
                }
                break;
            case GRID_3x3:
                strip = new BufferedImage(width * 3, height * 3, BufferedImage.TYPE_INT_RGB);
                g2d = strip.createGraphics();
                for (int i = 0; i < photos.size() && i < 9; i++) {
                    int x = (i % 3) * width;
                    int y = (i / 3) * height;
                    g2d.drawImage(photos.get(i).getImage(), x, y, null);
                }
                break;
            default:
                return null;
        }
        g2d.dispose();
        return strip;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public Layout getLayout() {
        return layout;
    }
}
