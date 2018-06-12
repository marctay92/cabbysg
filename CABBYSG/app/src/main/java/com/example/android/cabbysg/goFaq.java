package com.example.android.cabbysg;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class goFaq extends Fragment {

    public goFaq() {
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
        View rootView= inflater.inflate(R.layout.fragment_go_faq, container, false);
        // get the listview

        expListView = rootView.findViewById(R.id.lvExp);
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
        listDataHeader.add("How do I make a booking?");
        listDataHeader.add("How do I make payment?");
        listDataHeader.add("How do I cancel my booking?");
        listDataHeader.add("How do I report lost of items?");

        // Adding child data
        List<String> one = new ArrayList<String>();
        one.add("CabbySG is a ride-hailing application that acts as a shared platform amongst all taxi companies to utilize the entire fleet of approximately  23000 taxis as well as even more drivers to fulfill the needs of the consumers");

        List<String> two = new ArrayList<String>();
        two.add("CabbySG provides you the convenience of booking a ride without the uncertainty of traditional flagging of cab. Alos compared to other ride-hailing applications CabbySG has no surge pricing to ensure no rude surprise are waiting for you");

        List<String> three = new ArrayList<String>();
        three.add("CabbySG allows riders to see drivers in their vicinity before making the booking request, this is inform riders of the approximate waiting time based on the number of drivers around them. CabbySG then pairs a booking request to an available who will make their way to you!");

        List<String> four = new ArrayList<String>();
        four.add("Simple go to the Home page and input information in the fields below! Options to choose routes, car type, and fare types will allow you to choose the ride you most prefer");

        List<String> five = new ArrayList<String>();
        five.add("You can link your credit or debit card to your account by simply heading over to the payment page and add payment! Do not fret if you do not own these cards, simply pay by cash to the driver!");

        List<String> six = new ArrayList<String>();
        six.add("Simple click the 'x' button on your booking display on your Homepage, however please do not make frequent cancellation as it is rather vexing for our drivers. Thank you!");

        List<String> seven = new ArrayList<String>();
        seven.add("Go to the lost and found page and input the information into the fields provided to you! Do not worry if you forgot the ride you lost it on as that field is nullable");

        listDataChild.put(listDataHeader.get(0), one); // Header, Child data
        listDataChild.put(listDataHeader.get(1), two);
        listDataChild.put(listDataHeader.get(2), three);
        listDataChild.put(listDataHeader.get(3), four);
        listDataChild.put(listDataHeader.get(4), five);
        listDataChild.put(listDataHeader.get(5), six);
        listDataChild.put(listDataHeader.get(6), seven);
    }

}