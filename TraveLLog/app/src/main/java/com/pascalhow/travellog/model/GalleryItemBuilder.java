package com.pascalhow.travellog.model;

public class GalleryItemBuilder {
    private String title;
    private String imagePath;
    private String imageDescription;

    public GalleryItemBuilder setTitle(String title) {
        this.title = title;
        return this;
    }

    public GalleryItemBuilder setUrl(String imagePath) {
        this.imagePath = imagePath;
        return this;
    }

    public GalleryItemBuilder setImageDescription(String description) {
        this.imageDescription = description;
        return this;
    }

    public GalleryItem build() {
        return new GalleryItem(title, imagePath);
    }
}