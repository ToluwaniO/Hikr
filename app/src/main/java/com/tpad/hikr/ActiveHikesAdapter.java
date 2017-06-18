package com.tpad.hikr;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tpad.hikr.DataClasses.Hikes;

import java.util.ArrayList;

/**
 * Created by toluw on 6/17/2017.
 */

public class ActiveHikesAdapter extends RecyclerView.Adapter<ActiveHikesAdapter.ViewHolder> {

    ArrayList<Hikes> hikesList;

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView placeholder;
        TextView locationTxt, titleTxt, timeTxt;
        public ViewHolder(View itemView) {
            super(itemView);

            placeholder = (ImageView)itemView.findViewById(R.id.active_hikesImage);
            locationTxt = (TextView)itemView.findViewById(R.id.location_txt);
            titleTxt = (TextView)itemView.findViewById(R.id.title_txt);
            timeTxt = (TextView)itemView.findViewById(R.id.time_txt);
        }
    }

    public ActiveHikesAdapter(ArrayList<Hikes> hikesList) {
        this.hikesList = hikesList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.active_hikes_card, parent,false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.locationTxt.setText(hikesList.get(position).getLocation());
        holder.timeTxt.setText(hikesList.get(position).getTime());
        holder.titleTxt.setText(hikesList.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return hikesList.size();
    }

}
