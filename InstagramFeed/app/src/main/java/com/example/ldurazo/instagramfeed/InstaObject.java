package com.example.ldurazo.instagramfeed;

import android.graphics.Bitmap;

/**
 * Created by ldurazo on 7/15/2014.
 */
public class InstaObject {
    private String description;
    private String smallImage;
    private String largeImage;
    private Bitmap smallImageBitmap;
    private Bitmap largeImageBitmap;

    public Bitmap getSmallImageBitmap() {
        return smallImageBitmap;
    }

    public void setSmallImageBitmap(Bitmap smallImageBitmap) {
        this.smallImageBitmap = smallImageBitmap;
    }

    public Bitmap getLargeImageBitmap() {
        return largeImageBitmap;
    }

    public void setLargeImageBitmap(Bitmap largeImageBitmap) {
        this.largeImageBitmap = largeImageBitmap;
    }

    public String getLargeImage() {
        return largeImage;
    }

    public void setLargeImage(String largeImage) {
        this.largeImage = largeImage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSmallImage() {
        return smallImage;
    }

    public void setSmallImage(String smallImage) {
        this.smallImage = smallImage;
    }
}
