package com.tpad.hikr.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tpad.hikr.DataClasses.HikeItem;
import com.tpad.hikr.R;

import java.util.ArrayList;

/**
 * Created by toluw on 6/17/2017.
 */

public class ActiveHikesAdapter extends RecyclerView.Adapter<ActiveHikesAdapter.ViewHolder> {

    ArrayList<HikeItem> hikeItemList;

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

    public ActiveHikesAdapter(ArrayList<HikeItem> hikeItemList) {
        this.hikeItemList = hikeItemList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.active_hikes_card, parent,false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.locationTxt.setText(hikeItemList.get(position).getLocation());
        holder.timeTxt.setText(hikeItemList.get(position).getTime());
        holder.titleTxt.setText(hikeItemList.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return hikeItemList.size();
    }

}
