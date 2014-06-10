package com.internship.at5_savingdata.app;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;

public class MainActivity extends Activity {
    TextView name;
    TextView alias;
    final static String Name="nameKey";
    final static String Alias="aliasKey";
    File file = new File(this.getFilesDir(), "filename");
    FileOutputStream outputStream;
    SharedPreferences sharedpreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        name = (TextView) findViewById(R.id.nameText);
        alias = (TextView) findViewById(R.id.aliasText);

        sharedpreferences = getSharedPreferences("PREFERENCES", Context.MODE_PRIVATE);
        if (sharedpreferences.contains(Name))
        {
            name.setText(sharedpreferences.getString(Name, ""));

        }
        if (sharedpreferences.contains(Alias))
        {
            alias.setText(sharedpreferences.getString(Alias, ""));

        }

    }

    public void doStuff(View view){
        String n  = name.getText().toString();
        String a  = alias.getText().toString();
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(Name, n);
        editor.putString(Alias, a);
        editor.commit();

        try {
            outputStream = openFileOutput("filename", Context.MODE_PRIVATE);
            outputStream.write("esto se guardo por ahi en el celular".getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
