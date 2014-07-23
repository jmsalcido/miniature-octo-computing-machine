package com.example.ldurazo.instagramfeed;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by ldurazo on 7/15/2014.
 */
public class DownloadImage extends AsyncTask<String, Void, String>{
    String iurl;
    public DownloadImage(String iurl) throws IOException {
        this.iurl = iurl;
    }

    @Override
    protected String doInBackground(String... strings) {
        URL url = null;
        try {
            url = new URL(iurl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        InputStream input=null;
        try {
            input = url.openStream();
            //The sdcard directory e.g. '/sdcard' can be used directly, or
            //more safely abstracted with getExternalStorageDirectory()
            File storagePath = Environment.getExternalStorageDirectory();
            OutputStream output = new FileOutputStream(new File(storagePath, iurl.substring(iurl.length()/2)+".png"));
            Log.w("DI", "Downloading");
            try {
                byte[] buffer = new byte[2048];
                int bytesRead = 0;
                while ((bytesRead = input.read(buffer, 0, buffer.length)) >= 0) {
                    output.write(buffer, 0, bytesRead);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  null;
    }
}
