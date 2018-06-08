package com.example.android.cabbysg;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class nav_payment extends android.support.v4.app.Fragment {


    public nav_payment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_nav_payment, container, false);

        ArrayList<cardDetailsContainer> arrayOfDetails = new ArrayList<cardDetailsContainer>();
        addCardAdapter adapter = new addCardAdapter(getContext(),arrayOfDetails);
        ListView listView=(ListView)view.findViewById(R.id.cardList);
        listView.setAdapter(adapter);

        cardDetailsContainer newdetails=new cardDetailsContainer("1234123412341234");
        adapter.add(newdetails);

        Button button = view.findViewById(R.id.addpaymentmethod);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CardEditActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

}
