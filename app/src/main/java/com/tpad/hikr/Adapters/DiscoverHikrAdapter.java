package com.tpad.hikr.Adapters;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.tpad.hikr.DataClasses.DiscoverItem;
import com.tpad.hikr.R;

import java.util.ArrayList;
import java.util.zip.Inflater;

/**
 * Created by oguns on 7/16/2017.
 */

public class DiscoverHikrAdapter extends ArrayAdapter {
    ArrayList<DiscoverItem> items;
    public DiscoverHikrAdapter(@NonNull Context context, @LayoutRes int resource, ArrayList<DiscoverItem> items) {
        super(context, resource);
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
         return LayoutInflater.from(parent.getContext()).inflate(R.layout.discover_card, null);
    }
}
