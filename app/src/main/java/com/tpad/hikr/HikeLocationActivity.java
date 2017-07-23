package com.tpad.hikr;

import android.graphics.Bitmap;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.tpad.hikr.DataClasses.HikeLocationData;

public class HikeLocationActivity extends AppCompatActivity implements OnMapReadyCallback {

    MapFragment mapFragment;
    Boolean mapReady = false;
    GoogleMap m_map;
    HikeLocationData hikeLocationData;
    Bitmap bitmap;
    private static final String TAG = HikeLocationActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discover_item);

        Toolbar toolbar = (Toolbar)findViewById(R.id.discover_item_toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("");

        hikeLocationData = (HikeLocationData) getIntent().getParcelableExtra(HikeLocationData.class.getSimpleName());
//        if(hikeLocationData.getImage() == null){
//            Log.d(TAG, "image is null");
//        }
//        else {
//            Log.d(TAG, "Image exists");
//        }
        //bitmap = (Bitmap) getIntent().getParcelableExtra("image");
        Log.d(TAG, "rating " + hikeLocationData.getRating());
        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map_fragment);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapReady = true;
        m_map = googleMap;
        LatLng newYork = new LatLng(40.7484, -73.9857);
        CameraPosition target = CameraPosition.builder().target(newYork).zoom(14).build();
        m_map.moveCamera(CameraUpdateFactory.newCameraPosition(target));
    }
}
