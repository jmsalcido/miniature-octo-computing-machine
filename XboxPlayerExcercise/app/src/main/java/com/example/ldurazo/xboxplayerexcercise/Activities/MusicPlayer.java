package com.example.ldurazo.xboxplayerexcercise.activities;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController.MediaPlayerControl;

import com.example.ldurazo.xboxplayerexcercise.R;
import com.example.ldurazo.xboxplayerexcercise.adapters.DataWrapper;
import com.example.ldurazo.xboxplayerexcercise.controllers.MusicController;
import com.example.ldurazo.xboxplayerexcercise.controllers.MusicService;
import com.example.ldurazo.xboxplayerexcercise.controllers.MusicService.MusicBinder;
import com.example.ldurazo.xboxplayerexcercise.models.Track;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

public class MusicPlayer extends BaseActivity implements MediaPlayerControl{

    private static final String TAG="com.example.ldurazo.xboxplayerexcercise.activities.musicplayer";
    private ArrayList<Track> mTrackList;
    private ImageView mPlayerImageView;
    public static final String TRACK_LIST="TrackList";
    public static final String FIRST_TRACK="FirstTrack";
    private int currentTrack;
    private ImageLoader mImageLoader;

    // region Player Service
    //Player service variables
    private MusicService musicSrv;
    private Intent playIntent;
    private boolean musicBound=false;

    private ServiceConnection musicConnection = new ServiceConnection(){

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicBinder binder = (MusicBinder) service;
            //get service
            musicSrv = binder.getService();
            //pass list
            musicSrv.initTrack(mTrackList, currentTrack);
            musicSrv.playSong();
            musicBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicBound = false;
        }
    };

    //endregion

    //region LifeCycle

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);
        initUI();
        initVars();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(playIntent==null){
            playIntent = new Intent(this, MusicService.class);
            bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
            startService(playIntent);
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        displayImage();
        setController();
    }

    private void displayImage(){
        int imageWidth = mPlayerImageView.getWidth();
        int imageHeight = mPlayerImageView.getHeight();
        mImageLoader.displayImage(mTrackList.get(
                currentTrack).getImageURL()
                +"&w="+ imageWidth
                +"&h="+ imageHeight
                ,mPlayerImageView);
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


    //endregion

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
        switch (id) {
            case R.id.stop_song:
                stopService(playIntent);
                musicSrv=null;
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private MusicController controller;

    private void setController(){
        controller = new MusicController(MusicPlayer.this);
        controller.setAnchorView(mPlayerImageView);
        controller.setPrevNextListeners(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playNext();
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playPrev();
            }
        });
        controller.setMediaPlayer(this);
        controller.setEnabled(true);
    }

    //play next
    private void playNext(){
        musicSrv.playNext();
        controller.show(0);
    }

    //play previous
    private void playPrev(){
        musicSrv.playPrev();
        controller.show(0);
    }

//region Media Controllar
    @Override
    public void start() {
        musicSrv.go();
    }

    @Override
    public void pause() {
        musicSrv.pausePlayer();
    }

    @Override
    public int getDuration() {
        if(musicSrv!=null && musicBound && musicSrv.isPlaying())
        return musicSrv.getDur();
        else return 0;
    }

    @Override
    public int getCurrentPosition() {
        if(musicSrv!=null && musicBound && musicSrv.isPlaying())
        return musicSrv.getPosn();
        else return 0;
    }

    @Override
    public void seekTo(int pos) {
        musicSrv.seek(pos);
    }

    @Override
    public boolean isPlaying() {
        if(musicSrv!=null && musicBound)
        return musicSrv.isPlaying();
        return false;
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public boolean canPause() {
        return true;
    }

    @Override
    public boolean canSeekBackward() {
        return true;
    }

    @Override
    public boolean canSeekForward() {
        return true;
    }

    @Override
    public int getAudioSessionId() {
        return 0;
    }

    //endregion
}
