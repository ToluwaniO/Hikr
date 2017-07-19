package com.tpad.hikr.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.PlacePhotoMetadata;
import com.google.android.gms.location.places.PlacePhotoMetadataBuffer;
import com.google.android.gms.location.places.PlacePhotoMetadataResult;
import com.google.android.gms.location.places.Places;
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
    Bitmap bitmap;
    private static final String TAG = HikeLocationAdapater.class.getSimpleName();
    private final int LOCATION_TYPE = 0;
    private final int LOCATION_CARD_TYPE =1;
    GoogleApiClient googleApiClient;
    private LruCache<String, Bitmap> mMemoryCache;


    public HikeLocationAdapater(ArrayList<HikeLocationData> hikeLocationDataList, Context context, GoogleApiClient googleApiClient) {
        this.hikeLocationDataList = hikeLocationDataList;
        this.context = context;
        this.googleApiClient = googleApiClient;
        setUpCache();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name, city, distance;
        ImageView image;
        CardView cardView;
        RatingBar ratingBar;
        ProgressBar loadingBar;
        int type;
        public MyViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView)itemView.findViewById(R.id.discover_card);
            name = (TextView)itemView.findViewById(R.id.discover_name);
            city = (TextView)itemView.findViewById(R.id.discover_city_name);
            distance = (TextView)itemView.findViewById(R.id.discover_distance);
            image = (ImageView)itemView.findViewById(R.id.dicover_location_image);
            ratingBar = (RatingBar)itemView.findViewById(R.id.discover_rating_bar);
            loadingBar = (ProgressBar)itemView.findViewById(R.id.discover_image_loader);
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
                holder.distance.setText(String.valueOf(hikeLocationDataList.get(position).getDistance()));
                Bitmap b = getBitmapFromMemCache(hikeLocationDataList.get(position).getPlaceId());
                if(b != null){
                    Log.d(TAG, "Cache used");
                    holder.loadingBar.setVisibility(View.GONE);
                    holder.image.setVisibility(View.VISIBLE);
                    holder.image.setImageBitmap(b);
                }
                else {
                    new PhotoTask(holder.image.getMaxWidth(), holder.image.getMaxHeight(), position, holder).execute(hikeLocationDataList.get(position).getPlaceId());
                }

               //holder.image.setImageBitmap(hikeLocationDataList.get(position).getImage());
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
                Log.d(TAG, "id- " + hikeLocationDataList.get(position).getPlaceId());
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

    public void setUpCache(){
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

        // Use 1/8th of the available memory for this memory cache.
        final int cacheSize = maxMemory / 8;

        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                // The cache size will be measured in kilobytes rather than
                // number of items.
                return bitmap.getByteCount() / 1024;
            }
        };
    }

    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            mMemoryCache.put(key, bitmap);
            Log.d(TAG, "cache created");
        }
    }

    public Bitmap getBitmapFromMemCache(String key) {
        return mMemoryCache.get(key);
    }

    class PhotoTask extends AsyncTask<String, Void, PhotoTask.AttributedPhoto> {

        private int mHeight;

        private int mWidth;

        private int position;
        private MyViewHolder holder;
        public PhotoTask(int width, int height, int position, MyViewHolder holder) {
            mHeight = height;
            mWidth = width;
            this.position = position;
            this.holder = holder;
        }

        /**
         * Loads the first photo for a place id from the Geo Data API.
         * The place id must be the first (and only) parameter.
         */
        @Override
        protected AttributedPhoto doInBackground(String... params) {
            if (params.length != 1) {
                Log.d(TAG, "params length is null");
                return null;
            }
            final String placeId = params[0];
            AttributedPhoto attributedPhoto = null;

            PlacePhotoMetadataResult result = Places.GeoDataApi
                    .getPlacePhotos(googleApiClient, placeId).await();

            if (result.getStatus().isSuccess()) {
                PlacePhotoMetadataBuffer photoMetadataBuffer = result.getPhotoMetadata();
                if (photoMetadataBuffer.getCount() > 0 && !isCancelled()) {
                    // Get the first bitmap and its attributions.
                    PlacePhotoMetadata photo = photoMetadataBuffer.get(0);
                    CharSequence attribution = photo.getAttributions();
                    // Load a scaled bitmap for this photo.
                    Bitmap image = photo.getScaledPhoto(googleApiClient, mWidth, mHeight).await()
                            .getBitmap();

                    attributedPhoto = new AttributedPhoto(attribution, image);
                }
                // Release the PlacePhotoMetadataBuffer.
                photoMetadataBuffer.release();
            }
            return attributedPhoto;
        }

        @Override
        protected void onPostExecute(AttributedPhoto attributedPhoto) {
            if(attributedPhoto != null && attributedPhoto.bitmap != null){
                holder.loadingBar.setVisibility(View.GONE);
                holder.image.setVisibility(View.VISIBLE);
                holder.image.setImageBitmap(attributedPhoto.bitmap);
                addBitmapToMemoryCache(hikeLocationDataList.get(position).getPlaceId(), attributedPhoto.bitmap);
            }

        }

        /**
         * Holder for an image and its attribution.
         */
        class AttributedPhoto {

            public final CharSequence attribution;

            public final Bitmap bitmap;

            public AttributedPhoto(CharSequence attribution, Bitmap bitmap) {
                this.attribution = attribution;
                this.bitmap = bitmap;
            }
        }
    }
}
