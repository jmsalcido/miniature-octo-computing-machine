package com.internship.remindersfacebookapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.internship.remindersfacebookapp.app.R;
import com.internship.remindersfacebookapp.models.Reminder;
import com.internship.remindersfacebookapp.models.ViewHolder;

import java.util.List;
//THIS CLASS MAY BE NO LONGER USED
public class ReminderListAdapter extends BaseAdapter{
private List<Reminder> reminderList;
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
        ViewHolder holder;
        if(convertView==null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.reminders_row,null);
            holder = new ViewHolder();
            holder.contentView =(TextView) convertView.findViewById(R.id.reminder_content);
            holder.dateView = (TextView) convertView.findViewById(R.id.reminder_date);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
		holder.contentView.setText(reminderList.get(position).getContent());
		holder.dateView.setText(reminderList.get(position).getDate());

		return convertView;
	}
}
