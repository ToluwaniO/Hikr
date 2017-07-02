package com.tpad.hikr.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.tpad.hikr.DataClasses.Discover;
import com.tpad.hikr.DiscoverItem;
import com.tpad.hikr.R;

import java.util.ArrayList;

/**
 * Created by oguns on 6/30/2017.
 */

public class DiscoverAdapter extends RecyclerView.Adapter<DiscoverAdapter.MyViewHolder> {

    ArrayList<Discover> discoverList;
    Context context;

    public DiscoverAdapter(ArrayList<Discover> discoverList, Context context) {
        this.discoverList = discoverList;
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
        holder.name.setText(discoverList.get(position).getName());
        holder.city.setText(discoverList.get(position).getCity());
        holder.distance.setText(discoverList.get(position).getDistance());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Recycler", "clicked!");
                Intent intent = new Intent(context, DiscoverItem.class);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return discoverList.size();
    }

}
