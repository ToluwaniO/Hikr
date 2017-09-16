package com.tpad.hikr;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.tpad.hikr.DataClasses.HikeLocationData;
import com.tpad.hikr.DataClasses.Weather;
import com.tpad.hikr.WebUtils.QueryWeather;
import com.tpad.hikr.databinding.ActivityHikeLocationBinding;

import java.util.ArrayList;

public class HikeLocationActivity extends AppCompatActivity implements OnMapReadyCallback {

    MapFragment mapFragment;
    Boolean mapReady = false;
    GoogleMap m_map;
    HikeLocationData hikeLocationData;
    ActivityHikeLocationBinding binder;
    MyHandlers handlers;

    private static final String TAG = HikeLocationActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binder = DataBindingUtil.setContentView(this, R.layout.activity_hike_location);
        handlers = new MyHandlers();

        Toolbar toolbar = (Toolbar)findViewById(R.id.discover_item_toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("");

        ImageView locationView = (ImageView)findViewById(R.id.discover_item_image);

        hikeLocationData = (HikeLocationData) getIntent().getParcelableExtra(HikeLocationData.class.getSimpleName());
        LatLng latLng = new LatLng(hikeLocationData.getLatitude(), hikeLocationData.getLongitude());
        if(QueryAddress.Companion.getCityFromLatLng(latLng, this) == null)
        {
            hikeLocationData.setCity(QueryAddress.Companion.getCityFromAddress(hikeLocationData.getAddress()));
        }
        else{
            hikeLocationData.setCity(QueryAddress.Companion.getCityFromLatLng(latLng, this));
        }
        binder.setLData(hikeLocationData);
        binder.setHandlers(handlers);

        new GetWeatherData(latLng).execute(null, null, null);

        locationView.setImageBitmap(PictureCache.Companion.getPicture(hikeLocationData.getPlaceId(), this));
        Log.d(TAG, "rating " + hikeLocationData.getRating());
        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map_fragment);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapReady = true;
        m_map = googleMap;
        LatLng latLng = new LatLng(hikeLocationData.getLatitude(), hikeLocationData.getLongitude());
        googleMap.addMarker(new MarkerOptions().position(latLng)
                .title(hikeLocationData.getName()));
        CameraPosition target = CameraPosition.builder().target(latLng).zoom(14).build();
        m_map.moveCamera(CameraUpdateFactory.newCameraPosition(target));
    }

    public void populateWeatherView(ArrayList<Weather> list)
    {
        String url = "http://openweathermap.org/img/w/icon.png";
        binder.discoverItemForecastLayout.setVisibility(View.VISIBLE);
        binder.discoverItemForecastTitle.setVisibility(View.VISIBLE);
        binder.discoverItemDividerThree.setVisibility(View.VISIBLE);
        Glide.with(this).load(getdrawableID(list.get(0).getIcon())).into(binder.weatherDayOne);
        Glide.with(this).load(getdrawableID(list.get(1).getIcon())).into(binder.weatherDayTwo);
        Glide.with(this).load(getdrawableID(list.get(2).getIcon())).into(binder.weatherDayThree);
        Glide.with(this).load(getdrawableID(list.get(3).getIcon())).into(binder.weatherDayFour);
        Glide.with(this).load(getdrawableID(list.get(4).getIcon())).into(binder.weatherDayFive);
    }

    public int getdrawableID(String code){
        if(code.equals("01d")) return R.drawable.sun;
        else if(code.equals("02d") || code.equals("03d") || code.equals("04d")) return R.drawable.clouds;
        else if(code.equals("09d") || code.equals("10d")) return R.drawable.rain;
        else if(code.equals("11d")) return R.drawable.thunder_storm;
        else if(code.equals("13d")) return R.drawable.snow;
        else return R.drawable.mist;

    }

    class GetWeatherData extends AsyncTask<Void, Void, ArrayList<Weather>>{

        LatLng latLng;

        public GetWeatherData(LatLng latLng) {
            this.latLng = latLng;
        }

        @Override
        protected ArrayList<Weather> doInBackground(Void... params) {
            return QueryWeather.Companion.getWeatherData(latLng);
        }

        @Override
        protected void onPostExecute(ArrayList<Weather> weathers) {
            if(weathers != null && weathers.size() >= 5) {
                populateWeatherView(weathers);
            }
        }
    }
}
