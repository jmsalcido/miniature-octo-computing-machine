package com.example.ldurazo.xboxplayerexcercise.activities;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.MediaController.MediaPlayerControl;
import android.widget.TextView;

import com.example.ldurazo.xboxplayerexcercise.R;
import com.example.ldurazo.xboxplayerexcercise.adapters.DataWrapper;
import com.example.ldurazo.xboxplayerexcercise.adapters.SearchAdapter;
import com.example.ldurazo.xboxplayerexcercise.controllers.MusicService;
import com.example.ldurazo.xboxplayerexcercise.controllers.MusicService.MusicBinder;
import com.example.ldurazo.xboxplayerexcercise.controllers.ServiceChanges;
import com.example.ldurazo.xboxplayerexcercise.models.Track;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

public class MusicPlayerActivity extends BaseActivity implements MediaPlayerControl, ServiceChanges, DrawerLayout.DrawerListener{

    private static final String TAG="com.example.ldurazo.xboxplayerexcercise.activities.musicplayer";
    private ArrayList<Track> mTrackList;
    private ImageView mPlayerImageView;
    public static final String TRACK_LIST="TrackList";
    public static final String FIRST_TRACK="FirstTrack";
    private int mCurrentTrack;
    private ImageLoader mImageLoader;
    private TextView mNowPlayingTextView;
    private static MediaController controller;
    // region Player Service
    // Player service variables
    private MusicService musicSrv;
    private Intent playIntent;
    private boolean musicBound=false;

    private ServiceConnection musicConnection = new ServiceConnection(){

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicBinder binder = (MusicBinder) service;
            //get service
            musicSrv = binder.getService(MusicPlayerActivity.this);
            //pass list
            musicSrv.initTrack(mTrackList, mCurrentTrack);
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
        FrameLayout frameLayout = (FrameLayout)findViewById(R.id.content_frame);
        // inflate the custom activity layout
        LayoutInflater layoutInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View activityView = layoutInflater.inflate(R.layout.activity_music_player, null,false);
        // add the custom layout of this activity to frame layout.
        frameLayout.addView(activityView);
        if(playIntent==null){
            playIntent = new Intent(this, MusicService.class);
        }
        startService(playIntent);
        bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
        initUI();
        initVars();
        setController();
    }

    private void displayImageAndTextfield(int currentTrack){
        int imageWidth = mPlayerImageView.getWidth();
        int imageHeight = mPlayerImageView.getHeight();
        mImageLoader.displayImage(mTrackList.get(
                currentTrack).getImageURL()
                +"&w="+ imageWidth
                +"&h="+ imageHeight
                ,mPlayerImageView);
        mNowPlayingTextView.setText("Now playing: "+mTrackList.get(currentTrack).getName());
        mNowPlayingTextView.startAnimation(AnimationUtils.loadAnimation(MusicPlayerActivity.this, R.anim.abc_fade_in));
    }

    @Override
    protected void initUI() {
        mPlayerImageView = (ImageView) findViewById(R.id.PlayerImageView);
        mNowPlayingTextView = (TextView) findViewById(R.id.now_playing_text_view);
        mDrawerLayout.setDrawerListener(this);
        mPlayerImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!controller.isShowing())
                    controller.show();
            }
        });
    }

    @Override
    protected void initVars() {
        mImageLoader = ImageLoader.getInstance();
        mCurrentTrack = getIntent().getIntExtra(MusicPlayerActivity.FIRST_TRACK, 0);
        DataWrapper dw = (DataWrapper) getIntent().getSerializableExtra(TRACK_LIST);
        mTrackList= dw.getTracks();
        mDrawerList.setAdapter(new SearchAdapter(MusicPlayerActivity.this, mTrackList));
    }


    //endregion

    //region Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(mDrawerToggle.onOptionsItemSelected(item)){
            return true;
        }
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            default://TODO
            break;
        }
        return super.onOptionsItemSelected(item);
    }
    //endregion


    @Override
    public void onNewSongPlayed(int songPosition) {
        mCurrentTrack=songPosition;
        displayImageAndTextfield(songPosition);
    }



    @Override
    protected void onDestroy() {
        controller = null;
        unbindService(musicConnection);
        stopService(playIntent);
        super.onDestroy();
    }

    private void setController(){
        controller = new MediaController(MusicPlayerActivity.this);
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
        mDrawerLayout.openDrawer(findViewById(R.id.drawer));
        mDrawerLayout.closeDrawers();
    }

    //play previous
    private void playPrev(){
        musicSrv.playPrev();
    }

//region Media Controller
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


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            startActivity(new Intent().setAction(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME));
        }
        return super.onKeyDown(keyCode, event);
    }

    //endregion

    //region Drawer Listener
    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {
        //Do nothing
    }

    @Override
    public void onDrawerOpened(View drawerView) {
        Log.w(TAG, "Drawer Opened");
        controller.hide();
    }

    @Override
    public void onDrawerClosed(View drawerView) {
        Log.w(TAG, "Drawer closed");
        setController();
        controller.show();
    }

    @Override
    public void onDrawerStateChanged(int newState) {
    }

    //endregion
}
