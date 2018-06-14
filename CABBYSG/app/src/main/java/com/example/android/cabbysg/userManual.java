package com.example.android.cabbysg;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class userManual extends Fragment {
    ViewPager viewPager;
    riderManualAdapter adapter;

    public userManual(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView= inflater.inflate(R.layout.fragment_user_manual, container, false);
        viewPager= rootView.findViewById(R.id.riderPager);
        adapter = new riderManualAdapter(getActivity());
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(adapter.getCount()-5);
        return rootView;
    }
}
