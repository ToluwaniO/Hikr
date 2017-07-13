package com.tpad.hikr.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tpad.hikr.DataClasses.HikeLocationData;
import com.tpad.hikr.HikeLocationActivity;
import com.tpad.hikr.R;

import java.util.ArrayList;

/**
 * Created by oguns on 6/30/2017.
 */

public class HikeLocationAdapater extends RecyclerView.Adapter<HikeLocationAdapater.MyViewHolder> {

    ArrayList<HikeLocationData> hikeLocationDataList;
    Context context;

    public HikeLocationAdapater(ArrayList<HikeLocationData> hikeLocationDataList, Context context) {
        this.hikeLocationDataList = hikeLocationDataList;
        this.context = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name, city, distance;
        CardView cardView;
        public MyViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView)itemView.findViewById(R.id.discover_card);
            name = (TextView)itemView.findViewById(R.id.discover_name);
            city = (TextView)itemView.findViewById(R.id.discover_city_name);
            distance = (TextView)itemView.findViewById(R.id.discover_distance);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.discover_card, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.name.setText(hikeLocationDataList.get(position).getName());
        holder.city.setText(hikeLocationDataList.get(position).getCity());
        holder.distance.setText(hikeLocationDataList.get(position).getDistance());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Recycler", "clicked!");
                Intent intent = new Intent(context, HikeLocationActivity.class);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return hikeLocationDataList.size();
    }

}
