package com.internship.remindersfacebookapp.app;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView;
import android.widget.TextView;

import com.internship.remindersfacebookapp.adapters.ReminderListAdapter;
import com.internship.remindersfacebookapp.adapters.SQLiteAdapter;
import com.internship.remindersfacebookapp.models.Reminder;
import com.internship.remindersfacebookapp.models.RemindersUser;

public class RemindersFragment extends ListFragment {
private String header;
private RemindersUser mRemindersUser;
private View mView;
private SQLiteAdapter db;
    public RemindersFragment newInstance(String message) {
        this.setArguments(new Bundle(1));
        header = message;
        return this;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.getListView().setOnItemLongClickListener(new OnItemLongClickListener() {
            Reminder selectedReminder;
            public boolean onItemLongClick(AdapterView<?> arg0, View v, int index, long id) {
                selectedReminder =(Reminder) getListView().getItemAtPosition(index);
                final AlertDialog.Builder b = new AlertDialog.Builder(getActivity());
                b.setIcon(android.R.drawable.ic_dialog_info);
                b.setMessage("What do you want to do?");
                b.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        db.deleteSelectedReminder(selectedReminder.getContent(), selectedReminder.getDate());
                        refreshList(mView, db);
                    }
                }).setNegativeButton("Edit", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Intent reminderActivity = new Intent(getActivity(), AddReminderActivity.class);
                        reminderActivity.putExtra(RemindersUser.USERNAME, mRemindersUser.getName());
                        reminderActivity.putExtra(RemindersUser.MAIL, mRemindersUser.getMail());
                        reminderActivity.putExtra(RemindersUser.IMAGE, mRemindersUser.getImage());
                        reminderActivity.putExtra(RemindersUser.USER_ID, mRemindersUser.getUserId());
                        reminderActivity.putExtra(Reminder.CONTENT, selectedReminder.getContent());
                        reminderActivity.putExtra(Reminder.DATE, selectedReminder.getDate());
                        reminderActivity.putExtra(RemindersUser.FLAG,"EDIT");
                        startActivity(reminderActivity);
                    }
                });
                b.show();
                refreshList(mView, db);
                return true;
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.reminders_fragments, container, false);
	    db = new SQLiteAdapter(mView.getContext());
	    Bundle extras = getActivity().getIntent().getExtras();
        TextView textView = (TextView) mView.findViewById(R.id.profile_name);
        textView.setText(header);
	    mRemindersUser = new RemindersUser(
			    extras.getString(RemindersUser.USERNAME),
			    extras.getString(RemindersUser.MAIL),
			    extras.getString(RemindersUser.IMAGE),
                extras.getString(RemindersUser.USER_ID));
        refreshList(mView,db);

        return mView;
    }

    @Override
    public void onPause() {
        super.onPause();
        refreshList(mView,db);
    }

    @Override
    public void onResume() {
        //db.deleteAllReminders();
        super.onResume();
        refreshList(mView,db);
    }

    public void refreshList(View view, SQLiteAdapter db){
        if(header.equals(ViewPagerActivity.HEADER_1)){
            this.setListAdapter(new ReminderListAdapter(view.getContext(), db.selectReminder(mRemindersUser,1)));
        }
        if(header.equals(ViewPagerActivity.HEADER_2)){
            this.setListAdapter(new ReminderListAdapter(view.getContext(), db.selectReminder(mRemindersUser,0)));
        }
    }
}