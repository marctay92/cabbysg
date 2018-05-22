package com.example.android.cabbysg;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.Toast;
import android.widget.Toolbar;

import java.util.List;

public class faq extends AppCompatActivity {

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);

        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.lvExp);

        // preparing list data
        prepareListData();

        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);
    }

    /*
     * Preparing the list data
     */
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
        one.add("CabbySG is a ride-hailing application that acts as a shared platform amongst all taxi companies to utilize the entire fleet of approximately  23000 taxis as well as even more drivers to fulfill the needs of the consumers.");

        List<String> two = new ArrayList<String>();
        two.add("Reasons to use");

        List<String> three = new ArrayList<String>();
        three.add("Methods it works");

        List<String> four = new ArrayList<String>();
        four.add("Ways to make booking");

        List<String> five = new ArrayList<String>();
        five.add("Money");

        List<String> six = new ArrayList<String>();
        six.add("Run Away");

        List<String> seven = new ArrayList<String>();
        seven.add("Call police");

        listDataChild.put(listDataHeader.get(0), one); // Header, Child data
        listDataChild.put(listDataHeader.get(1), two);
        listDataChild.put(listDataHeader.get(2), three);
        listDataChild.put(listDataHeader.get(3), four);
        listDataChild.put(listDataHeader.get(4), five);
        listDataChild.put(listDataHeader.get(5), six);
        listDataChild.put(listDataHeader.get(6), seven);
    }
}

