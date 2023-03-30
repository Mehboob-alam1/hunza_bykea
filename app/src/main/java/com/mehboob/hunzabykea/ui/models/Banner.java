package com.mehboob.hunzabykea.ui.models;

public class Banner {
    private String bannerUrl;
    private int bannerImage;
    private String id;

    public String getBannerUrl() {
        return bannerUrl;
    }

    public void setBannerUrl(String bannerUrl) {
        this.bannerUrl = bannerUrl;
    }

    public int getBannerImage() {
        return bannerImage;
    }

    public void setBannerImage(int bannerImage) {
        this.bannerImage = bannerImage;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Banner(String bannerUrl, String id) {

        this.bannerUrl = bannerUrl;
        this.id = id;
    }

    public Banner(int bannerImage, String id) {
        this.bannerImage = bannerImage;
        this.id = id;
    }
}
