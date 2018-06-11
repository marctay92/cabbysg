package com.example.android.cabbysg;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class nav_history extends Fragment {
    FirebaseUser user;
    String userId;
    ArrayList<historyDetails>arrayOfDetails=new ArrayList<historyDetails>();
    String startLocationStr,driverIdStr,destinationStr,selectedRouteStr,fareStr, historyID;
    ListView listView;
    historyAdapter adapter;

    public nav_history() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_nav_history, container, false);

        user = FirebaseAuth.getInstance().getCurrentUser();
        userId = user.getUid();

        listView = rootView.findViewById(R.id.listOfDetails);

        //final
        adapter = new historyAdapter (getActivity(),getDataSetHistory());
        listView.setAdapter(adapter);

        getUserHistoryIds();

        /*historyDetails newhistoryDetails = new historyDetails("1234","Driver1","Joshua","Kwek","Toyota Altis","SKW2733D","NLB","SIM","09 June 2018","12:29","Shortest","10.00","5");
        adapter.add(newhistoryDetails);*/
        return rootView;
    }

    private void getUserHistoryIds(){
        DatabaseReference userHistory_db = FirebaseDatabase.getInstance().getReference().child("Rider").child(user.getUid()).child("History");
        userHistory_db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot scheduledRide : dataSnapshot.getChildren()){
                        historyID = scheduledRide.getKey();
                        FetchHistoryInfo(historyID);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void FetchHistoryInfo(String rideKey){
        DatabaseReference historyDatabase = FirebaseDatabase.getInstance().getReference().child("History").child(rideKey);
        arrayOfDetails.clear();
        historyDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    Long timestamp = 0L;
                    for(DataSnapshot child: dataSnapshot.getChildren()){
                        if(child.getKey().equals("driverID")) {
                            driverIdStr = child.getValue().toString();
                        }
                        if(child.getKey().equals("startLocation")){
                            startLocationStr = child.getValue().toString();
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
                    historyDetails obj = new historyDetails(historyID,driverIdStr,startLocationStr,destinationStr,getDate(timestamp),getTime(timestamp),selectedRouteStr,fareStr);
                    arrayOfDetails.add(obj);
                    adapter.notifyDataSetChanged();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private ArrayList<historyDetails> getDataSetHistory(){
        return arrayOfDetails;
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