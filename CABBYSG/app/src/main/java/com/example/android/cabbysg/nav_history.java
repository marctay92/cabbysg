package com.example.android.cabbysg;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class nav_history extends Fragment {

    public nav_history() {
        // Required empty public constructor
    }

    ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_nav_history, container, false);

        ArrayList<historyDetails>arrayOfDetails=new ArrayList<historyDetails>();

        historyAdapter adapter = new historyAdapter (getActivity(),arrayOfDetails);

        listView = (ListView)rootView.findViewById(R.id.listOfDetails);
        listView.setAdapter(adapter);

        historyDetails newhistoryDetails = new historyDetails("Joshua","1234","010618","33");
        adapter.add(newhistoryDetails);

        return rootView;
    }

}
