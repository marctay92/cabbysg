package com.example.android.cabbysg;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

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
public class nav_schedule extends Fragment {

    DatabaseReference schedule_db;
    FirebaseUser user;
    ArrayList<schedule_details>arrayOfSchedule=new ArrayList<schedule_details>();
    ListView listView;

    public nav_schedule() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_nav_schedule, container, false);

        listView = rootView.findViewById(R.id.schedulelist);

//MARCUS CHANGE TO DATABASE
        user = FirebaseAuth.getInstance().getCurrentUser();
        schedule_db = FirebaseDatabase.getInstance().getReference("RiderSchedule").child(user.getUid());

        listView = rootView.findViewById(R.id.listOfDetails);

        schedule_db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayOfSchedule.clear();
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                    if (user.getUid().equals(postSnapshot.getKey())){
                        schedule_details scheduleTrip = postSnapshot.getValue(schedule_details.class);
                        arrayOfSchedule.add(scheduleTrip);
                    }
                }
                schedule_adaptor adapter = new schedule_adaptor(getActivity(), arrayOfSchedule);
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return rootView;
    }

}
