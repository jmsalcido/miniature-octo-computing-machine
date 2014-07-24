package com.example.ldurazo.xboxplayerexcercise.models;

////A track object can be either an artist, an album or a track itself
//as the xbox music service when performing the search it returns the same values for the
//three of them: id, name, imageurl and the type is for the app to know if the object is an artist
// or a track or an album in order to do the proper workflow after the search.
public class Track {
    private String Id;
    private String name;
    private String imageURL;
    private String type;

    public Track(String id, String name, String imageURL, String TAG) {
        Id = id;
        this.name = name;
        this.imageURL = imageURL;
        this.type = TAG;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}