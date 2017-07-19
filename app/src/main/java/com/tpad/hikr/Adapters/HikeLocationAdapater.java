package com.tpad.hikr.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.tpad.hikr.HikeLocationActivity;
import com.tpad.hikr.R;

import java.util.ArrayList;

/**
 * Created by oguns on 6/30/2017.
 */

public class HikeLocationAdapater extends RecyclerView.Adapter<HikeLocationAdapater.MyViewHolder> {

    ArrayList<HikeLocationData> hikeLocationDataList;
    Context context;
    Bitmap bitmap;
    private static final String TAG = HikeLocationAdapater.class.getSimpleName();
    private final int LOCATION_TYPE = 0;
    private final int LOCATION_CARD_TYPE =1;

    public HikeLocationAdapater(ArrayList<HikeLocationData> hikeLocationDataList, Context context) {
        this.hikeLocationDataList = hikeLocationDataList;
        this.context = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name, city, distance;
        ImageView image;
        CardView cardView;
        RatingBar ratingBar;
        int type;
        public MyViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView)itemView.findViewById(R.id.discover_card);
            name = (TextView)itemView.findViewById(R.id.discover_name);
            city = (TextView)itemView.findViewById(R.id.discover_city_name);
            distance = (TextView)itemView.findViewById(R.id.discover_distance);
            image = (ImageView)itemView.findViewById(R.id.dicover_location_image);
            ratingBar = (RatingBar)itemView.findViewById(R.id.discover_rating_bar);
        }
        public MyViewHolder(View itemView, int type){
            super(itemView);
            name = (TextView)itemView.findViewById(R.id.location_txt_view);
            this.type = type;
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;

        switch (viewType){
            case LOCATION_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.current_location_layout, parent, false);
                return new MyViewHolder(view, 1);
            default:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.discover_card, parent, false);
                break;
        }

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        Log.d(TAG, "TYPE - " + holder.getItemViewType());

        switch (holder.getItemViewType()){

            case LOCATION_TYPE:
                holder.name.setText(hikeLocationDataList.get(position).getCity());
                break;
            default:
                holder.name.setText(hikeLocationDataList.get(position).getName());
                holder.city.setText(hikeLocationDataList.get(position).getAddress());
                holder.distance.setText(hikeLocationDataList.get(position).getDistance());
                holder.image.setImageBitmap(hikeLocationDataList.get(position).getImage());
                holder.ratingBar.setMax(5);
                Log.d(TAG, (float)hikeLocationDataList.get(position).getRating() + "");
                holder.ratingBar.setRating((float) hikeLocationDataList.get(position).getRating());

                holder.cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d("Recycler", "clicked!");
                        Intent intent = new Intent(context, HikeLocationActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putParcelable(HikeLocationData.class.getSimpleName(), hikeLocationDataList.get(position));
                        //intent.putExtra("image", hikeLocationDataList.get(position).getImage());
                        intent.putExtras(bundle);
                        context.startActivity(intent);
                    }
                });
                break;
        }
    }

    @Override
    public int getItemCount() {
        return hikeLocationDataList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0) return LOCATION_TYPE;
        return LOCATION_CARD_TYPE;
    }
}
