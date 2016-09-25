package com.pascalhow.travellog.model;

public class MyTripsItemBuilder {
    private String title;
    private String imagePath;
    private String imageDescription;

    public MyTripsItemBuilder setTitle(String title) {
        this.title = title;
        return this;
    }

    public MyTripsItemBuilder setUrl(String imagePath) {
        this.imagePath = imagePath;
        return this;
    }

    public MyTripsItemBuilder setImageDescription(String description) {
        this.imageDescription = description;
        return this;
    }

    public MyTripsItem build() {
        return new MyTripsItem(title, imagePath);
    }
}