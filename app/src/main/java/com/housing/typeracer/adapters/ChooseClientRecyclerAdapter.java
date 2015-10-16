package com.housing.typeracer.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.housing.typeracer.R;
import com.housing.typeracer.models.Client;

import java.util.List;

/**
 * Created by Rohit Arya (http://rohitarya.com/) on 16/10/15.
 */
public class ChooseClientRecyclerAdapter extends RecyclerView.Adapter<ChooseClientRecyclerAdapter.ViewHolder> {

    private List<Client> dataSet;
    ColorGenerator generator;
    TextDrawable.IBuilder builder;

    public ChooseClientRecyclerAdapter(List<Client> dataSet) {
        this.dataSet = dataSet;
        generator = ColorGenerator.MATERIAL;
        builder = TextDrawable.builder()
                .round();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.client_row_item, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String name = dataSet.get(position).getRemoteEndpointName();
        holder.clientName.setText(name);
        int color = generator.getColor(name);
        holder.textDrawable.setImageDrawable(builder.build(name.substring(0, 1), color));
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView clientName;
        ImageView textDrawable;

        public ViewHolder(View itemView) {
            super(itemView);
            clientName = (TextView) itemView.findViewById(R.id.client_name);
            textDrawable = (ImageView) itemView.findViewById(R.id.text_drawable);
        }
    }
}
