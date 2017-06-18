package com.tpad.hikr;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by toluw on 6/15/2017.
 */

public class HomeFragment extends Fragment {

    String displayName, imageUrl;
    TextView nameView, hikeNumberView, hikeDistanceView;
    ImageView profilePic;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.home_fragment_layout, container, false);

        nameView = (TextView)rootView.findViewById(R.id.user_name);
        hikeNumberView = (TextView)rootView.findViewById(R.id.hike_number);
        hikeDistanceView = (TextView)rootView.findViewById(R.id.hike_distance);
        profilePic = (ImageView)rootView.findViewById(R.id.user_imgView);

        Bundle bundle = this.getArguments();

        if(bundle != null){
            imageUrl = bundle.getString("imageUrl");
            displayName = bundle.getString("name");

            nameView.setText(displayName);
            Glide.with(this).load(imageUrl).into(profilePic);
        }

        return rootView;
    }
}
