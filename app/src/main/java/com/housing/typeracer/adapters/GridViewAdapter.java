package com.housing.typeracer.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.housing.typeracer.R;
import com.housing.typeracer.Utils;

import java.util.ArrayList;

/**
 * Created by gayathri_nair on 17/10/15.
 */
public class GridViewAdapter extends BaseAdapter {
    private ArrayList<Integer> dataList = new ArrayList<>();
    private Context c;

    public GridViewAdapter(Context c) {
        this.dataList = new ArrayList<>();
        dataList.add(R.drawable.robo_1);
        dataList.add(R.drawable.robo_2);
        dataList.add(R.drawable.robo_3);
        dataList.add(R.drawable.robo_4);
        dataList.add(R.drawable.robo_5);
        dataList.add(R.drawable.robo_6);
        dataList.add(R.drawable.robo_7);
        dataList.add(R.drawable.robo_8);
        dataList.add(R.drawable.robo_9);
        this.c = c;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) c
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.grid_item, parent, false);
        ImageView avatar = (ImageView) v.findViewById(R.id.robo_avatar);
        final ImageView check = (ImageView) v.findViewById(R.id.grid_check);
        v.setLayoutParams(new GridView.LayoutParams((int) Utils
                .convertDpToPixel(c, 86), (int) Utils
                .convertDpToPixel(c, 87))); // hdpi and below
        avatar.setImageResource(dataList.get(position));
        return v;
    }
}
