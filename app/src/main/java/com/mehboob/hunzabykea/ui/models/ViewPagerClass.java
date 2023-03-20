package com.mehboob.hunzabykea.ui.models;

public class ViewPagerClass {

    private int image;
    private String heading,description;

    public ViewPagerClass(int image, String heading, String description) {
        this.image = image;
        this.heading = heading;
        this.description = description;
    }

    public ViewPagerClass() {
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
