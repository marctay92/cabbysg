package com.example.android.cabbysg;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class nav_driverhistory extends Fragment {


    public nav_driverhistory() {
    }

    FirebaseDatabase database;
    DatabaseReference reference;
    DatabaseReference driverRef;
    FirebaseUser user;
    String userID,thisMonth;
    String startLocationStr,riderIdStr,destinationStr,selectedRouteStr,fareStr, historyID;
    ListView listView;
    ArrayList<driverhistoryitem>arrayOfDetails=new ArrayList<>();
    driverhistoryadapter adapter;
    TextView month1, month2, month3, sumfare1, sumfare2, sumfare3;
    String stoday,spast,spast2;
    LinearLayout dHistoryList;
    RelativeLayout monthlyList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_nav_driverhistory, container, false);


        database=FirebaseDatabase.getInstance();
        reference=database.getReference("History");
        user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getUid();
        month1= v.findViewById(R.id.thismonth);
        month2= v.findViewById(R.id.lastmonth);
        month3= v.findViewById(R.id.monthbefore);
        sumfare1= v.findViewById(R.id.firstvalue);
        sumfare2= v.findViewById(R.id.secondvalue);
        sumfare3= v.findViewById(R.id.thirdvalue);
        monthlyList = v.findViewById(R.id.monthlyHistory);
        dHistoryList = v.findViewById(R.id.detailedHistory);

        listView = v.findViewById(R.id.dList);
        adapter = new driverhistoryadapter (getActivity(),getDataSetHistory());
        listView.setAdapter(adapter);


        Calendar cal=Calendar.getInstance();
        String today=new SimpleDateFormat("MMM").format(cal.getTime());
        stoday =new SimpleDateFormat("MM").format(cal.getTime());
        int x=Integer.parseInt(stoday);
        x--;
        stoday=Integer.toString(x);
        month1.setText(today);

        cal.add(Calendar.MONTH,-1);
        String past=new SimpleDateFormat("MMM").format(cal.getTime());
        spast=new SimpleDateFormat("MM").format(cal.getTime());
        int y=Integer.parseInt(spast);
        y--;
        spast=Integer.toString(y);
        month2.setText(past);

        cal.add(Calendar.MONTH,-1);
        String past2=new SimpleDateFormat("MMM").format(cal.getTime());
        spast2=new SimpleDateFormat("MM").format(cal.getTime());
        int z=Integer.parseInt(spast2);
        z--;
        spast2=Integer.toString(z);
        month3.setText(past2);

        driverRef = FirebaseDatabase.getInstance().getReference().child("Drivers").child(userID).child("History").child(spast2);
        driverRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    double ffare =0.00;
                    for (DataSnapshot month : dataSnapshot.getChildren()) {
                        String fare = month.child("fare").getValue().toString();
                        ffare = ffare + Double.parseDouble(fare);
                        //driverRef.removeEventListener(driverRefListener);
                    }
                    String fare3 = Double.toString(ffare);
                    sumfare3.setText(fare3);
                } else {
                    sumfare3.setText("Nothing");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        driverRef = FirebaseDatabase.getInstance().getReference().child("Drivers").child(userID).child("History").child(spast);
        driverRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    double ffare =0.00;
                    for (DataSnapshot month : dataSnapshot.getChildren()) {
                        String fare = month.child("fare").getValue().toString();
                        ffare = ffare + Double.parseDouble(fare);
                        //driverRef.removeEventListener(driverRefListener);
                    }
                    String fare2 = Double.toString(ffare);
                    sumfare2.setText(fare2);
                } else {
                    sumfare2.setText("Nothing");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        driverRef = FirebaseDatabase.getInstance().getReference().child("Drivers").child(userID).child("History").child(stoday);
        driverRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    double ffare =0.00;
                    for (DataSnapshot month : dataSnapshot.getChildren()) {
                        String fare = month.child("fare").getValue().toString();
                        ffare = ffare + Double.parseDouble(fare);
                        //driverRef.removeEventListener(driverRefListener);
                    }
                    String fare1 = Double.toString(ffare);
                    sumfare1.setText(fare1);
                } else {
                    sumfare1.setText("Nothing");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        if(!sumfare1.getText().equals("Nothing")) {
            month1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    monthlyList.setVisibility(View.INVISIBLE);
                    dHistoryList.setVisibility(View.VISIBLE);
                    //arrayOfDetails.clear();
                    int x=arrayOfDetails.size();
                    String y= Integer.toString(x);
                    Log.d("array number", y );
                    /*listView = v.findViewById(R.id.dList);
                    adapter = new driverhistoryadapter (getActivity(),getDataSetHistory());
                    listView.setAdapter(adapter);*/
                    getUserMonthlyHistoryIds(stoday);
                }
            });
        }
        if(!sumfare2.getText().equals("Nothing")) {
            month2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    monthlyList.setVisibility(View.INVISIBLE);
                    dHistoryList.setVisibility(View.VISIBLE);
                    //arrayOfDetails.clear();
                    getUserMonthlyHistoryIds(spast);
                }
            });
        }
        if(!sumfare3.getText().equals("Nothing")) {
            month3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    monthlyList.setVisibility(View.INVISIBLE);
                    dHistoryList.setVisibility(View.VISIBLE);
                    //arrayOfDetails.clear();
                    getUserMonthlyHistoryIds(spast2);
                }
            });
        }
        return v;
    }
    private void getUserMonthlyHistoryIds(String month){
        DatabaseReference userHistory_db = FirebaseDatabase.getInstance().getReference().child("Drivers").child(user.getUid()).child("History").child(month);
        userHistory_db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot history : dataSnapshot.getChildren()){
                        historyID = history.getKey();
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
        historyDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    Long timestamp = 0L;
                    for(DataSnapshot child: dataSnapshot.getChildren()){
                        if(child.getKey().equals("riderID")) {
                            riderIdStr = child.getValue().toString();
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
                    driverhistoryitem obj = new driverhistoryitem(historyID,riderIdStr,startLocationStr,destinationStr,getDate(timestamp),getTime(timestamp),selectedRouteStr,fareStr);
                    arrayOfDetails.add(obj);
                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private ArrayList<driverhistoryitem> getDataSetHistory(){
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
