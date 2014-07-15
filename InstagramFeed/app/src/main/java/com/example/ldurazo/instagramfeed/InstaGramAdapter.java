package com.example.ldurazo.instagramfeed;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;


public class InstaGramAdapter extends BaseAdapter {
    private List<InstaObject> instaList;
    private Context mContext;

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    public InstaGramAdapter(Context context, List<InstaObject> results) {
        instaList = results;
        mContext = context;
    }

    public int getCount() {
        return instaList.size();
    }

    public Object getItem(int position) {
        return instaList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.row_view, parent, false);
        TextView descriptionTextView = (TextView) rowView.findViewById(R.id.smallDescription);
        ImageView profilePicVew = (ImageView) rowView.findViewById(R.id.icon);
        profilePicVew.setImageBitmap(instaList.get(position).getSmallImageBitmap());
        descriptionTextView.setText(instaList.get(position).getDescription());
        return rowView;
    }

}
