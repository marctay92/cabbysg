package com.example.android.cabbysg;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class driverManual extends Fragment {

    ViewPager viewPager;
    ViewPageAdapter adapter;

    public driverManual() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView= inflater.inflate(R.layout.fragment_driver_manual, container, false);

        viewPager= rootView.findViewById(R.id.viewPager);
        adapter = new ViewPageAdapter(getActivity());
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(adapter.getCount()-7);

        return rootView;
    }

}
