package com.internship.remindersfacebookapp.app;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.internship.remindersfacebookapp.adapters.ReminderListAdapter;
import com.internship.remindersfacebookapp.adapters.SQLiteAdapter;
import com.internship.remindersfacebookapp.models.RemindersUser;

public class RemindersFragment extends Fragment {
private String header;
private RemindersUser mRemindersUser;
private View mView;
private SQLiteAdapter db;
private static final String DELETE = "Delete";
private static final String EDIT = "Edit";
private ListView mListView;
    public RemindersFragment newInstance(String message) {
        this.setArguments(new Bundle(1));
        header = message;
        return this;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(Menu.NONE,Menu.NONE,Menu.NONE, DELETE);
        menu.add(Menu.NONE,Menu.NONE,Menu.NONE, EDIT);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if(item.getTitle().equals(DELETE)){
            //TODO
        }
        if(item.getTitle().equals(EDIT)){
           //TODO
        }
        return true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.reminders_fragments, container, false);
	    db = new SQLiteAdapter(mView.getContext());
	    Bundle extras = getActivity().getIntent().getExtras();
        TextView textView = (TextView) mView.findViewById(R.id.profile_name);
        textView.setText(header);
        mListView = (ListView) mView.findViewById(R.id.listView);
	    mRemindersUser = new RemindersUser(
			    extras.getString(RemindersUser.USERNAME),
			    extras.getString(RemindersUser.MAIL),
			    extras.getString(RemindersUser.IMAGE),
                extras.getString(RemindersUser.USER_ID));
        refreshList(mView,db);
        registerForContextMenu(mListView);
        return mView;
    }

    @Override
    public void onResume() {
        //db.deleteAllReminders();
        refreshList(mView,db);
        super.onResume();
    }

    public void refreshList(View view, SQLiteAdapter db){
        if(header.equals(ViewPagerActivity.HEADER_1)){
            mListView.setAdapter(new ReminderListAdapter(view.getContext(), db.selectReminder(mRemindersUser, 1)));
        }
        if(header.equals(ViewPagerActivity.HEADER_2)){
            mListView.setAdapter(new ReminderListAdapter(view.getContext(), db.selectReminder(mRemindersUser, 0)));
        }
    }
}