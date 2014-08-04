package com.example.ldurazo.xboxplayerexcercise.controllers;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.example.ldurazo.xboxplayerexcercise.asynctasks.StreamAsyncTask;
import com.example.ldurazo.xboxplayerexcercise.asynctasks.StreamCallback;
import com.example.ldurazo.xboxplayerexcercise.models.Track;

import java.util.ArrayList;

public class MusicService extends Service implements
        MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener,
        MediaPlayer.OnCompletionListener, StreamCallback {

    private static final String TAG = "com.example.ldurazo.xboxplayerexcercise.controllers.musicservice";
    //media player
    private MediaPlayer player;
    //song list
    private ArrayList<Track> trackList;
    //current position
    private int songPosn;
    private final IBinder musicBind = new MusicBinder();

    public void initTrack(ArrayList<Track> trackList, int songPosn){
        this.trackList = trackList;
        this.songPosn = songPosn;
    }

    public class MusicBinder extends Binder {
        public MusicService getService() {
            return MusicService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //initialize position
        songPosn=0;
        //create player
        player = new MediaPlayer();
        initPlayer();
    }

    public void initPlayer(){
        player.setOnPreparedListener(this);
        player.setOnCompletionListener(this);
        player.setOnErrorListener(this);
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        player.stop();
        player.release();
        return false;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return musicBind;
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {

    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, int i, int i2) {
        return false;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        //TODO enable the line below when the notification in onPrepared is ready
        //stopForeground(true);
    }

    //region PlayerMethods
    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        mediaPlayer.start();
        //TODO navigation to go back to the player
        /*
        Intent notIntent = new Intent(this, MusicPlayer.class);
        notIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendInt = PendingIntent.getActivity(this, 0,
                notIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder builder = new Notification.Builder(this);

        builder.setContentIntent(pendInt)
                .setSmallIcon(android.R.drawable.ic_media_play)
                .setTicker(trackList.get(songPosn).getName())
                .setOngoing(true)
                .setContentTitle(trackList.get(songPosn).getName())
        .setContentText(trackList.get(songPosn).getName());
        Notification notification = builder.build();
        startForeground(1, notification);
        */
    }

    public void playPrev(){
        songPosn--;
        if(songPosn>0 && songPosn<=trackList.size()){
            playSong();
        }else{
            songPosn++;
        }
    }

    public void playNext(){
        songPosn++;
        if(songPosn>0 && songPosn<=trackList.size()){
            playSong();
        }else{
            songPosn--;
        }
    }

    String trackUrl;
    public void playSong(){
        player.reset();
        //get song
        Track playSong = trackList.get(songPosn);
        //get id
        String currSong = playSong.getId();
        //set uri
        new StreamAsyncTask(trackList.get(songPosn), this).execute();

    }

    @Override
    public void onStreamReceived(String streamURL) {
        Log.w(TAG, streamURL);
        trackUrl = streamURL;
        Uri trackUri = Uri.parse(trackUrl);
        try{
            player.setDataSource(getApplicationContext(), trackUri);
        }
        catch(Exception e){
            Log.e("MUSIC SERVICE", "Error setting data source", e);
        }
        player.prepareAsync();
    }

    //endregion

    //region Mediacontroller
    public int getPosn(){
        return player.getCurrentPosition();
    }

    public int getDur(){
        return player.getDuration();
    }

    public boolean isPlaying(){
        return player.isPlaying();
    }

    public void pausePlayer(){
        player.pause();
    }

    public void seek(int posn){
        player.seekTo(posn);
    }

    public void go(){
        player.start();
    }
    //endregion
}
