package com.example.ldurazo.xboxplayerexcercise.applications;

import android.app.Application;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;


public class ImageLoaderApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //Create ImageLoaderConfiguration
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .diskCacheFileCount(50)
                .diskCacheExtraOptions(2000,2000,null)
                .writeDebugLogs()
                .build();

        //Initialize ImageLoader with configuration
        ImageLoader.getInstance().init(config);
    }

}
