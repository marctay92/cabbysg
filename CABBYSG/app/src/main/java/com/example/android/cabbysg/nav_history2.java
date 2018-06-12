package com.example.android.cabbysg;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class nav_history2 extends Fragment {

    DatabaseReference history_db;
    FirebaseUser user;
    ArrayList<driverhistoryitem> arrayOfDetails=new ArrayList<driverhistoryitem>();
    ListView listView;
    TextView fare;
    TextView tripDate;
    TextView tripTime;
    TextView startLocation;
    TextView finalLocation;
    TextView riderName;
    TextView tripType;
    TextView riderRating;
    //ImageView driverProfilePic;

    public nav_history2() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView=inflater.inflate(R.layout.fragment_nav_history2, container, false);

        //driverProfilePic=(ImageView)rootView.findViewById(R.id.history2Profile);
        /*listView = rootView.findViewById(R.id.dList);

        driverhistoryadapter adapter = new driverhistoryadapter (getActivity(),arrayOfDetails);
        listView.setAdapter(adapter);

        driverhistoryitem newdhistory = new driverhistoryitem("1234","Driver1","Joshua","Kwek","NLB","SIM","09 June 2018","12:29","Shortest","10.00","5");
        adapter.add(newdhistory);*/
        return rootView;
    }
}
