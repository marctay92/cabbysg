package com.example.android.cabbysg;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.json.JSONArray;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class nav_schedule extends Fragment {


    public nav_schedule() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_nav_schedule, container, false);
        ArrayList <schedule_details> arrayOfUsers = new ArrayList<schedule_details>();

        schedule_adaptor adapter = new schedule_adaptor(getActivity(), arrayOfUsers);

        ListView listView = (ListView)rootView.findViewById(R.id.schedulelist);
        listView.setAdapter(adapter);
//MARCUS CHANGE TO DATABASE
        schedule_details newschedule_details = new schedule_details("14 April 2018", "20:16", "John Tan",
                "SHJ3982P", "3.9", "Orchard Ion", "Ubi Mrt Station",
                "Fastest", "20.00");

                adapter.add(newschedule_details);
        return rootView;
    }

}
