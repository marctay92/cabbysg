package com.example.android.cabbysg;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class nav_history extends Fragment {
    DatabaseReference history_db;
    FirebaseUser user;
    ArrayList<historyDetails>arrayOfDetails=new ArrayList<historyDetails>();
    ListView listView;

    public nav_history() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_nav_history, container, false);

        user = FirebaseAuth.getInstance().getCurrentUser();
        history_db = FirebaseDatabase.getInstance().getReference("RiderHistory").child(user.getUid()).child("1");

        listView = rootView.findViewById(R.id.listOfDetails);

        final historyAdapter adapter = new historyAdapter (getActivity(),arrayOfDetails);
        listView.setAdapter(adapter);

        history_db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayOfDetails.clear();
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                    if (user.getUid().equals(postSnapshot.getKey())){
                        historyDetails tripHistory = postSnapshot.getValue(historyDetails.class);
                        adapter.addAll(tripHistory);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        /*historyDetails newhistoryDetails = new historyDetails("Joshua","SHA1234A","1 APRIL 2018","13:00","33.00");
        adapter.add(newhistoryDetails);*/
        return rootView;
    }

}
