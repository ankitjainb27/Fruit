package com.housing.typeracer.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.housing.typeracer.MainApplication;
import com.housing.typeracer.R;
import com.housing.typeracer.models.ListItem;

import java.util.ArrayList;

/**
 * Created by gayathri_nair on 17/10/15.
 */
public class LvAdapter extends BaseAdapter {

    private ArrayList<ListItem> data;

    public LvAdapter(ArrayList<ListItem> data) {
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) MainApplication.getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.lv_item, parent, false);

            holder.icon = (ImageView) convertView.findViewById(R.id.profimg);
            holder.bar = (ProgressBar) convertView.findViewById(R.id.bar);
            holder.speed = (TextView) convertView.findViewById(R.id.speed);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        return convertView;
    }

    class ViewHolder {
        ImageView icon;
        ProgressBar bar;
        TextView speed;

    }
}
