package com.example.ldurazo.xboxplayerexcercise.adapters;

import com.example.ldurazo.xboxplayerexcercise.models.Track;

import java.io.Serializable;
import java.util.ArrayList;


public class DataWrapper implements Serializable {
    private ArrayList<Track> tracks;

    public DataWrapper(ArrayList<Track> tracks) {
        this.tracks = tracks;
    }

    public ArrayList<Track> getTracks() {
        return tracks;
    }
}
