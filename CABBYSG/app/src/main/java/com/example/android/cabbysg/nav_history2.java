package com.example.android.cabbysg;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class nav_history2 extends Fragment {

    TextView fare;
    TextView tripDate;
    TextView tripTime;
    TextView startLocation;
    TextView finalLocation;
    TextView driverName;
    TextView driverVeh;
    TextView carModel;
    TextView tripType;
    TextView driverRating;
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
        tripDate=(TextView)rootView.findViewById(R.id.history2Date);
        tripTime=(TextView)rootView.findViewById(R.id.history2Time);
        driverName=(TextView)rootView.findViewById(R.id.history2Name);
        driverVeh=(TextView)rootView.findViewById(R.id.history2CarPlate);
        carModel=(TextView)rootView.findViewById(R.id.history2CarModel);
        fare=(TextView)rootView.findViewById(R.id.history2Fare);
        driverRating=(TextView)rootView.findViewById(R.id.history2Rating);
        tripType=(TextView)rootView.findViewById(R.id.history2Route);
        startLocation=(TextView)rootView.findViewById(R.id.history2From);
        finalLocation=(TextView)rootView.findViewById(R.id.history2To);


        return rootView;

    }


}
