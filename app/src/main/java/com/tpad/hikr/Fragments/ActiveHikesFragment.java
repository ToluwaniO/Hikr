package com.tpad.hikr.Fragments;

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
import com.tpad.hikr.R;

import java.util.ArrayList;

/**
 * Created by toluw on 6/17/2017.
 */

public class ActiveHikesFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.active_hikes_fragment_layout, container, false);


        return rootView;
    }
}