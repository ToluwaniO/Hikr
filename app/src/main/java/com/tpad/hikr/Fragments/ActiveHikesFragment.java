package com.tpad.hikr.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tpad.hikr.R;

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