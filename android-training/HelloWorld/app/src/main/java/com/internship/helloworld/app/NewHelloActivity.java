package com.internship.helloworld.app;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.TextView;


public class NewHelloActivity extends ActionBarActivity {
    int i=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final TextView textView = new TextView(this);
        setContentView(textView);
        textView.startAnimation(AnimationUtils.loadAnimation(NewHelloActivity.this, android.R.anim.fade_in));
        textView.setTextSize(60);
        textView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                while(true) {
                    if (i == 0) {
                        textView.setText("Hallo, Welt!");
                        textView.startAnimation(AnimationUtils.loadAnimation(NewHelloActivity.this, android.R.anim.slide_out_right));
                        i = 1;
                        break;
                    }
                    if (i == 1) {
                        textView.startAnimation(AnimationUtils.loadAnimation(NewHelloActivity.this, android.R.anim.slide_in_left));
                        textView.setText("Hello, World!");
                        i = 0;
                        break;
                    }
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.new_hello, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



}
