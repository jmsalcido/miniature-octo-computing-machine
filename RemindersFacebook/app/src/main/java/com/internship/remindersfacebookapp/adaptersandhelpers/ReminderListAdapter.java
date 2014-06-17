package com.internship.remindersfacebookapp.adaptersandhelpers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.internship.remindersfacebookapp.app.R;
import com.internship.remindersfacebookapp.models.Reminder;

import java.util.List;

public class ReminderListAdapter extends BaseAdapter{
private static List<Reminder> reminderList;
private Context mContext;

	public ReminderListAdapter(Context context, List<Reminder> results) {
		reminderList = results;
		mContext = context;
	}

	@Override
	public int getCount() {
		return reminderList.size();
	}

	@Override
	public Object getItem(int position) {
		return reminderList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.reminders_row, parent, false);
		TextView contentView = (TextView) rowView.findViewById(R.id.reminder_content);
		TextView dateView = (TextView) rowView.findViewById(R.id.reminder_date);
		contentView.setText(reminderList.get(position).getContent());
		dateView.setText(reminderList.get(position).getDate());

		return rowView;
	}
}
