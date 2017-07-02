package com.tpad.hikr.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.tpad.hikr.Adapters.DiscoverAdapter;
import com.tpad.hikr.DataClasses.Discover;
import com.tpad.hikr.DiscoverItem;
import com.tpad.hikr.R;

import java.util.ArrayList;

/**
 * Created by toluw on 6/15/2017.
 */

public class HomeFragment extends Fragment {
    RecyclerView recyclerView;
    ArrayList<Discover> discoverArrayList;
    DiscoverAdapter discoverAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.home_fragment_layout, container, false);

        discoverArrayList = new ArrayList<>();
        discoverAdapter = new DiscoverAdapter(discoverArrayList, rootView.getContext());

        recyclerView = (RecyclerView)rootView.findViewById(R.id.discover_recycler);

        setRecyclerView(rootView);


        return rootView;
    }

    private void setRecyclerView(View v){

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(v.getContext(), 1);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(discoverAdapter);

        populate();
    }

    private void populate(){
        for (int i = 0; i < 20; i ++){
            discoverArrayList.add(new Discover("Des Fees Lake Trail", "Gatineau, QC", "24km"));
            discoverAdapter.notifyDataSetChanged();
        }
    }
}
