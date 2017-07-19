package com.tpad.hikr.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.android.gms.common.api.GoogleApiClient;
import com.tpad.hikr.Adapters.HikeLocationAdapater;
import com.tpad.hikr.DataClasses.HikeLocationData;
import com.tpad.hikr.MainNavActivity;
import com.tpad.hikr.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by toluw on 6/15/2017.
 */

public class HomeFragment extends Fragment {
    RecyclerView recyclerView;
    ProgressBar progressBar;
    ArrayList<HikeLocationData> hikeLocationDataArrayList;
    HikeLocationAdapater hikeLocationAdapater;
    private static final String TAG = HomeFragment.class.getSimpleName();
    String location;
    View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.home_fragment_layout, container, false);
        hikeLocationDataArrayList = new ArrayList<>();

        progressBar = (ProgressBar)rootView.findViewById(R.id.home_progressbar);
        recyclerView = (RecyclerView)rootView.findViewById(R.id.discover_recycler);
        //setRecyclerView(rootView);

        return rootView;
    }

    private void setRecyclerView(){
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(rootView.getContext(), 1);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(hikeLocationAdapater);
        //populate();
    }

    public void populate(List<HikeLocationData> data, GoogleApiClient googleApiClient){
        //hikeLocationDataArrayList.add(new HikeLocationData("Waiting..."));
        //hikeLocationAdapater.notifyDataSetChanged();
        hikeLocationAdapater = new HikeLocationAdapater(hikeLocationDataArrayList, rootView.getContext(), googleApiClient);
        setRecyclerView();
        for (int i = 0; i < data.size(); i ++){
            hikeLocationDataArrayList.add(data.get(i));
            hikeLocationAdapater.notifyDataSetChanged();
        }
        progressBar.setVisibility(View.GONE);
        Log.d(TAG, data.size() + "");
        Log.d(TAG, "View populated");
    }

    public void onLocationFound(String location) {
        Log.d(TAG, location);
        this.location = location;
        if(hikeLocationDataArrayList.size()>0){
            hikeLocationDataArrayList.get(0).setCity(location);
            hikeLocationAdapater.notifyDataSetChanged();
        }
    }
}
