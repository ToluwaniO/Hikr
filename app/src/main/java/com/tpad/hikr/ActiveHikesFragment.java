package com.tpad.hikr;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tpad.hikr.DataClasses.Hikes;

import java.util.ArrayList;

/**
 * Created by toluw on 6/17/2017.
 */

public class ActiveHikesFragment extends Fragment {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    ActiveHikesAdapter adapter;
    ArrayList<Hikes> hikesList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("Active Frag", "adapter added");
        View rootView = inflater.inflate(R.layout.active_hikes_fragment_layout, container, false);

        hikesList = new ArrayList<>();
        adapter = new ActiveHikesAdapter(hikesList);

        recyclerView = (RecyclerView)rootView.findViewById(R.id.active_hikes_recycler);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(rootView.getContext(), 1);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.setAdapter(adapter);

        populateAdapter();



        return rootView;
    }

    public void populateAdapter(){
        String location = "Gatineau Park, QC";
        String title = "Des Fees Lake Trail";
        String time = "2 hours ago";

        for (int i = 0; i < 10; i++){
            hikesList.add(new Hikes(title, location, time));
            adapter.notifyDataSetChanged();
        }
    }
}
