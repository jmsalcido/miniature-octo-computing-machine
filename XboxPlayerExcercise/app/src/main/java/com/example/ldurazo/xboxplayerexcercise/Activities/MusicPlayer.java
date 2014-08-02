package com.example.ldurazo.xboxplayerexcercise.activities;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.example.ldurazo.xboxplayerexcercise.R;
import com.example.ldurazo.xboxplayerexcercise.adapters.DataWrapper;
import com.example.ldurazo.xboxplayerexcercise.models.Track;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.IOException;
import java.util.ArrayList;

public class MusicPlayer extends BaseActivity {

    private static final String TAG="com.example.ldurazo.xboxplayerexcercise.activities.musicplayer";
    private ArrayList<Track> mTrackList;
    private ImageView mPlayerImageView;
    private int imageWidth;
    private int imageHeight;
    public static final String TRACK_LIST="TrackList";
    public static final String FIRST_TRACK="FirstTrack";
    private int currentTrack = 0;
    private ImageLoader mImageLoader;
    private MediaPlayer mediaPlayer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);
        initUI();
        initVars();
        //region media player example (delete when finished)
        Uri myUri = Uri.parse("http://searchgurbani.com/audio/sggs/1.mp3");
        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setDataSource(this, myUri);
            mediaPlayer.prepare(); //don't use prepareAsync for mp3 playback
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //endregion
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        imageWidth=mPlayerImageView.getWidth();
        imageHeight=mPlayerImageView.getHeight();
        mImageLoader.displayImage(mTrackList.get(currentTrack).getImageURL()+"&w="+imageWidth+"&h="+ imageHeight, mPlayerImageView);

    }

    @Override
    protected void onResume() {
       super.onResume();
    }

    @Override
    protected void initUI() {
        mPlayerImageView = (ImageView) findViewById(R.id.PlayerImageView);
    }

    @Override
    protected void initVars() {
        mImageLoader = ImageLoader.getInstance();
        currentTrack = getIntent().getIntExtra(MusicPlayer.FIRST_TRACK, 0);
        DataWrapper dw = (DataWrapper) getIntent().getSerializableExtra(TRACK_LIST);
        mTrackList= dw.getTracks();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.music_player, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }


}
