package com.example.ldurazo.instagramfeed;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;

public class DetailActivity extends Activity{
    String image_url;
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        imageView = (ImageView) findViewById(R.id.imageView);
        TextView textView = (TextView) findViewById(R.id.textView);
        image_url = getIntent().getStringExtra("LARGE_IMAGE");
        String description = getIntent().getStringExtra("CONTENT");
        textView.setText(description);
        new LoadBigImage(imageView).execute(image_url);
        imageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                try {
                    new DownloadImage(image_url);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return true;
            }
        });
    }

}
