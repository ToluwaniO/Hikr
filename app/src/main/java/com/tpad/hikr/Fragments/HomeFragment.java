package com.tpad.hikr.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tpad.hikr.Adapters.HikeLocationAdapater;
import com.tpad.hikr.DataClasses.HikeLocationData;
import com.tpad.hikr.R;

import java.util.ArrayList;

/**
 * Created by toluw on 6/15/2017.
 */

public class HomeFragment extends Fragment {
    RecyclerView recyclerView;
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

        recyclerView = (RecyclerView)rootView.findViewById(R.id.discover_recycler);
        setRecyclerView(rootView);

        return rootView;
    }

    private void setRecyclerView(View v){
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(v.getContext(), 1);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(hikeLocationAdapater);
        populate();
    }

    private void populate(){
        hikeLocationDataArrayList.add(new HikeLocationData("Des Fees Lake Trail", "Ottawa", "24km"));
        hikeLocationAdapater.notifyDataSetChanged();
        for (int i = 1; i < 20; i ++){
            hikeLocationDataArrayList.add(new HikeLocationData("Des Fees Lake Trail", "Gatineau, QC", "24km"));
            hikeLocationAdapater.notifyDataSetChanged();
        }
    }

    public void onLocationFound(String location) {
        this.location = location;
        //populate();
    }
}
