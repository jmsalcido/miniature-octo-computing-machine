package com.example.ldurazo.xboxplayerexcercise.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ldurazo.xboxplayerexcercise.R;
import com.example.ldurazo.xboxplayerexcercise.adapters.SearchAdapter;
import com.example.ldurazo.xboxplayerexcercise.asynctasks.OnSearchTaskCallback;
import com.example.ldurazo.xboxplayerexcercise.asynctasks.SearchAsyncTask;
import com.example.ldurazo.xboxplayerexcercise.models.Constants;
import com.example.ldurazo.xboxplayerexcercise.models.AppSession;
import com.example.ldurazo.xboxplayerexcercise.models.Track;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

public class SearchActivity extends BaseActivity implements OnSearchTaskCallback {
    private static final String TAG = "com.example.ldurazo.xboxplayerexcercise.activities.baseactivity";
    private ProgressDialog dialog;
    TextView textView;
    EditText editText;
    RadioButton artistRadioButton;
    RadioButton albumRadioButton;
    RadioButton songRadioButton;
    private ListView listView;
    private InputMethodManager imm;
    private Track mTrack;
    private SearchAsyncTask searchAsyncTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initVars();
        initUI();
    }

    @Override
    protected void initVars() {
        dialog = new ProgressDialog(SearchActivity.this);
        dialog.setProgressStyle(R.style.AppTheme);
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    @Override
    protected void initUI() {
        textView = (TextView) findViewById(R.id.textView);
        editText = (EditText) findViewById(R.id.editText);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId== EditorInfo.IME_ACTION_DONE){
                    searchForResult();
                }
                return false;
            }
        });
        artistRadioButton = (RadioButton) findViewById(R.id.artist_radio_button);
        albumRadioButton = (RadioButton) findViewById(R.id.album_radio_button);
        songRadioButton = (RadioButton) findViewById(R.id.song_radio_button);
        listView = (ListView) findViewById(R.id.listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mTrack = (Track) listView.getItemAtPosition(i);
                Log.w(TAG, mTrack.getName()+mTrack.getId());
            }
        });
    }

    public void searchForResult() {
        if (!dialog.isShowing()) {
            dialog.show();
        }
        String search_query;
        String searchType;
        if (!editText.getText().toString().equals(Constants.EMPTY_STRING)) {
            search_query = editText.getText().toString();
            try {
                search_query = URLEncoder.encode(search_query, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            searchType = getSearchType();
            // this is hardcoded access token for testing purposes, use it instead of AppSession.getInstance().getAccessToken()
            // http%253a%252f%252fschemas.xmlsoap.org%252fws%252f2005%252f05%252fidentity%252fclaims%252fnameidentifier%3Dmusicplayer_internship_ldurazo%26http%253a%252f%252fschemas.microsoft.com%252faccesscontrolservice%252f2010%252f07%252fclaims%252fidentityprovider%3Dhttps%253a%252f%252fdatamarket.accesscontrol.windows.net%252f%26Audience%3Dhttp%253a%252f%252fmusic.xboxlive.com%26ExpiresOn%3D1406831538%26Issuer%3Dhttps%253a%252f%252fdatamarket.accesscontrol.windows.net%252f%26HMACSHA256%3DIbUBiV9cxaxmORMnCU38%252b1jiJlDV2fUHLOX0CF6rDGo%253d
            searchAsyncTask = new SearchAsyncTask(AppSession.getInstance().getAccessToken(), search_query, searchType, this);
            searchAsyncTask.execute();
        } else {
            hideDialog();
            Toast.makeText(SearchActivity.this, "Please introduce a search text", Toast.LENGTH_SHORT).show();
        }
    }

    private String getSearchType() {
        if (artistRadioButton.isChecked()) {
            return Track.ARTISTS;
        }
        if (albumRadioButton.isChecked()) {
            return Track.ALBUMS;
        }
        if (songRadioButton.isChecked()) {
            return Track.TRACKS;
        }
        return Track.TRACKS;
    }

    @Override
    public void onSearchCompleted(ArrayList<Track> list, int errorFlag) {
        hideDialog();
        if (list != null) {
            listView.setAdapter(new SearchAdapter(SearchActivity.this, list));
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.search_action) {
            searchForResult();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent().setAction(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME));
    }

    public void hideDialog() {
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
    }
}
