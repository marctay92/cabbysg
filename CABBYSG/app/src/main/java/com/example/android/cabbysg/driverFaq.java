package com.example.android.cabbysg;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class driverFaq extends Fragment {


    public driverFaq() {
        // Required empty public constructor
    }

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_driver_faq, container, false);

        expListView = rootView.findViewById(R.id.driverLvExp);
        // preparing list data

        prepareListData();

        listAdapter = new ExpandableListAdapter(getContext(), listDataHeader, listDataChild);
        // setting list adapter
        expListView.setAdapter(listAdapter);

        return rootView;
    }

    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data
        listDataHeader.add("What is CabbySG?");
        listDataHeader.add("Why should I use CabbySG?");
        listDataHeader.add("How does CabbySG works?");
        listDataHeader.add("How do I accept/decline a booking?");
        listDataHeader.add("How do I receive payment?");
        listDataHeader.add("How do I cancel a booking?");
        listDataHeader.add("How do I report items found?");

        // Adding child data
        List<String> one = new ArrayList<String>();
        one.add("CabbySG is a ride-hailing application that acts as a shared platform amongst all taxi companies to utilize the entire fleet of approximately  23000 taxis as well as even more drivers to fulfill the needs of the consumers.");

        List<String> two = new ArrayList<String>();
        two.add("You no longer have to drive around hoping for a flag down, or risk summon from LTA when you pick up a passenger from bus stops or double zigzag yellow lines and pick them up in their car parks instead");

        List<String> three = new ArrayList<String>();
        three.add("When a booking is requested in your vicinity, a request will be pass to you to accept or decline. If you are currently unavailable please set your status to offline by clicking the tab on the top right hand corner of the screen to not receive any notifications");

        List<String> four = new ArrayList<String>();
        four.add("When a booking request is send to you, simply swipe right to accept and left to decline!");

        List<String> five = new ArrayList<String>();
        five.add("You can claim your monthly earnings from your company on the 1st Friday of every month");

        List<String> six = new ArrayList<String>();
        six.add("Simple click the 'x' button on your booking display on your Homepage, however frequent cancellation will affect your ratings so please take note!");

        List<String> seven = new ArrayList<String>();
        seven.add("Go to the lost and found page and input the information into the fields provided to you! Remember to upload the photo so we can return the lost property to its owner as soon as possible");

        listDataChild.put(listDataHeader.get(0), one); // Header, Child data
        listDataChild.put(listDataHeader.get(1), two);
        listDataChild.put(listDataHeader.get(2), three);
        listDataChild.put(listDataHeader.get(3), four);
        listDataChild.put(listDataHeader.get(4), five);
        listDataChild.put(listDataHeader.get(5), six);
        listDataChild.put(listDataHeader.get(6), seven);
    }


}
