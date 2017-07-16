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
import android.widget.TextView;

import com.tpad.hikr.Adapters.HikeLocationAdapater;
import com.tpad.hikr.DataClasses.HikeLocationData;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.home_fragment_layout, container, false);

        hikeLocationDataArrayList = new ArrayList<>();
        hikeLocationAdapater = new HikeLocationAdapater(hikeLocationDataArrayList, rootView.getContext());

        progressBar = (ProgressBar)rootView.findViewById(R.id.home_progressbar);
        recyclerView = (RecyclerView)rootView.findViewById(R.id.discover_recycler);
        setRecyclerView(rootView);

        return rootView;
    }

    private void setRecyclerView(View v){
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(v.getContext(), 1);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(hikeLocationAdapater);
        //populate();
    }

    public void populate(List<HikeLocationData> data){
        hikeLocationDataArrayList.add(new HikeLocationData("Des Fees Lake Trail", "Ottawa", "24km"));
        hikeLocationAdapater.notifyDataSetChanged();
        for (int i = 0; i < data.size(); i ++){
            hikeLocationDataArrayList.add(data.get(i));
            hikeLocationAdapater.notifyDataSetChanged();
        }
        progressBar.setVisibility(View.GONE);
    }

    public void onLocationFound(String location) {
        Log.d(TAG, "onLocationFound called");
        this.location = location;
        if(hikeLocationDataArrayList.size()>0){
            hikeLocationDataArrayList.get(0).setCity(location);
            hikeLocationAdapater.notifyDataSetChanged();
        }
    }
}
