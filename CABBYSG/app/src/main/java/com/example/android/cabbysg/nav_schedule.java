package com.example.android.cabbysg;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class nav_schedule extends Fragment {

    FirebaseUser user;
    String userId;
    String currentLocationStr,driverIdStr,destinationStr,selectedRouteStr,fareStr,driverFullNameStr,driverRatingStr,regNumStr,driverFirstNameStr,driverLastNameStr,scheduleID;
    ArrayList<schedule_details>arrayOfSchedule=new ArrayList<schedule_details>();
    ListView listView;
    schedule_adaptor adapter;

    public nav_schedule() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_nav_schedule, container, false);

//MARCUS CHANGE TO DATABASE
        user = FirebaseAuth.getInstance().getCurrentUser();
        userId = user.getUid();

        listView = rootView.findViewById(R.id.listOfDetails);

        adapter = new schedule_adaptor(getActivity(), getDataSetHistory());

        listView = rootView.findViewById(R.id.schedulelist);
        listView.setAdapter(adapter);

        getUserScheduledIds();

        return rootView;
    }
    private void getUserScheduledIds(){
        DatabaseReference userScheduleDatabase = FirebaseDatabase.getInstance().getReference().child("Rider").child(userId).child("scheduleRides");
        userScheduleDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot scheduledRide : dataSnapshot.getChildren()){
                        scheduleID = scheduledRide.getKey();
                        FetchScheduledInfo(scheduledRide.getKey());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void FetchScheduledInfo(String rideKey){
        DatabaseReference scheduledRidesDatabase = FirebaseDatabase.getInstance().getReference().child("scheduledRides").child(rideKey);
        arrayOfSchedule.clear();
        scheduledRidesDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    Long timestamp = 0L;
                    for(DataSnapshot child: dataSnapshot.getChildren()){
                        if(child.getKey().equals("driverID")) {
                            driverIdStr = child.getValue().toString();
                            DatabaseReference driver_db = FirebaseDatabase.getInstance().getReference().child("Drivers").child(driverIdStr);
                            driver_db.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()) {
                                        for (DataSnapshot child1 : dataSnapshot.getChildren()) {
                                            if (child1.getKey().equals("firstName")) {
                                                driverFirstNameStr = child1.getValue().toString();
                                            }
                                            if(child1.getKey().equals("lastName")){
                                                driverLastNameStr = child1.getValue().toString();
                                            }
                                            if(child1.getKey().equals("regNum")) {
                                                regNumStr = child1.getValue().toString();
                                            }
                                            if (child1.getKey().equals("rating")) {
                                                driverRatingStr = child1.getValue().toString();
                                            }
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }else {
                            driverFirstNameStr = "";
                            driverLastNameStr = "";
                            regNumStr = "";
                            driverRatingStr = "";
                        }
                        if(child.getKey().equals("currentLocation")){
                            currentLocationStr = child.getValue().toString();
                        }
                        if(child.getKey().equals("destination")){
                            destinationStr = child.getValue().toString();
                        }

                        if(child.getKey().equals("selectedRoute")){
                            selectedRouteStr = child.getValue().toString();
                        }

                        if(child.getKey().equals("fare")){
                            fareStr = child.getValue().toString();
                        }

                        if(child.getKey().equals("timestamp")){
                            timestamp = Long.valueOf(child.getValue().toString());
                        }

                    }
                    driverFullNameStr = driverFirstNameStr + " " + driverLastNameStr;
                    schedule_details obj = new schedule_details(scheduleID,getDate(timestamp),getTime(timestamp),driverIdStr,driverFullNameStr,regNumStr,driverRatingStr,destinationStr,currentLocationStr,selectedRouteStr,fareStr);
                    arrayOfSchedule.add(obj);
                    adapter.notifyDataSetChanged();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private ArrayList<schedule_details> getDataSetHistory(){
        return arrayOfSchedule;
    }

    private String getDate(Long timestamp){
        Calendar cal = Calendar.getInstance(Locale.getDefault());
        cal.setTimeInMillis(timestamp*1000);
        String date = DateFormat.format("dd-MM-yyyy",cal).toString();
        return date;
    }
    private String getTime(Long timestamp){
        Calendar cal = Calendar.getInstance(Locale.getDefault());
        cal.setTimeInMillis(timestamp*1000);
        String time = DateFormat.format("hh:mm",cal).toString();
        return time;
    }


}

    /*@Override
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
    }*/