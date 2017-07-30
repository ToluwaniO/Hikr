package com.tpad.hikr.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tpad.hikr.Adapters.DiscoverHikrAdapter;
import com.tpad.hikr.DataClasses.DiscoverItem;
import com.tpad.hikr.R;
import com.wenchao.cardstack.CardStack;

import java.util.ArrayList;

/**
 * Created by oguns on 7/16/2017.
 */

public class HikrDiscoverFragment extends Fragment {
    CardStack cardStack;
    DiscoverHikrAdapter discoverHikrAdapter;
    ArrayList<DiscoverItem> list;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.hikr_discover_fragment, container, false);
        list = new ArrayList<>();
//        for(int i = 0; i < 10; i++){
//            discoverHikrAdapter.add(new DiscoverItem());
//        }
//        cardStack = (CardStack)view.findViewById(R.id.container);
//        cardStack.setContentResource(R.layout.location_card);
//        discoverHikrAdapter = new DiscoverHikrAdapter(getContext(), R.layout.location_card,list);
//        cardStack.setAdapter(discoverHikrAdapter);
        return view;
    }
}
