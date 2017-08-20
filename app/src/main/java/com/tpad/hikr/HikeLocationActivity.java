package com.tpad.hikr;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.tpad.hikr.DataClasses.HikeLocationData;
import com.tpad.hikr.databinding.ActivityHikeLocationBinding;

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

    public static void getU(){

    }
}
