package com.housing.typeracer.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.housing.typeracer.MainApplication;
import com.housing.typeracer.R;
import com.housing.typeracer.models.Host;

import java.util.List;

/**
 * Created by Rohit Arya (http://rohitarya.com/) on 16/10/15.
 */
public class ChooseHostRecyclerAdapter extends RecyclerView.Adapter<ChooseHostRecyclerAdapter.ViewHolder> {

    private List<Host> dataSet;
    ColorGenerator generator;
    TextDrawable.IBuilder builder;

    public ChooseHostRecyclerAdapter(List<Host> dataSet) {
        this.dataSet = dataSet;
        generator = ColorGenerator.MATERIAL;
        builder = TextDrawable.builder()
                .round();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.host_row_item, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String name = dataSet.get(position).getEndpointName();
        String actualName = MainApplication.USER_NAME.get(name);
        if (actualName != null) {
            name = actualName;
        }
        holder.hostName.setText(name);
        int color = generator.getColor(name);
        holder.textDrawable.setImageDrawable(builder.build(name.substring(0, 1), color));
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView hostName;
        ImageView textDrawable;

        public ViewHolder(View itemView) {
            super(itemView);
            hostName = (TextView) itemView.findViewById(R.id.host_name);
            textDrawable = (ImageView) itemView.findViewById(R.id.text_drawable);
        }
    }
}
