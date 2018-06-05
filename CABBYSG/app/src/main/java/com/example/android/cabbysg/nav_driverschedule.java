package com.example.android.cabbysg;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class nav_driverschedule extends Fragment {


    public nav_driverschedule() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_nav_driverschedule, container, false);
        ArrayList <driverschedule_details> arrayOfUsers = new ArrayList<driverschedule_details>();

        driverschedule_adaptor adapter = new driverschedule_adaptor(getActivity(), arrayOfUsers);

        ListView listView = (ListView)rootView.findViewById(R.id.driverschedulelist);
        listView.setAdapter(adapter);

//MARCUS CHANGE TO DATABASE

        driverschedule_details newdriverschedule_details = new driverschedule_details("14 April 2018", "13:41", "Mary Tan",
                "4.5", "Orchard Ion", "Ubi Mrt Station", "Fastest",
                "13.00", "With a luggage");

        adapter.add(newdriverschedule_details);
        return rootView;
    }


}
