package com.example.ldurazo.xboxplayerexcercise.adapters;

import com.example.ldurazo.xboxplayerexcercise.models.Track;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by ldurazo on 7/24/2014 and 3:33 PM.
 */
public class DataWrapper implements Serializable {
    private ArrayList<Track> tracks;

    public DataWrapper(ArrayList<Track> tracks) {
        this.tracks = tracks;
    }

    public ArrayList<Track> getTracks() {
        return tracks;
    }
}
