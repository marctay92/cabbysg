package com.example.android.cabbysg;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class nav_driverhistory extends Fragment {


    public nav_driverhistory() {
    }

    FirebaseDatabase database;
    DatabaseReference reference;
    DatabaseReference driverRef, driverRef2, driverRef3;
    ValueEventListener driverRef2Listener, driverRef3Listener;
    FirebaseUser user;
    String userID,thisMonth;
    TextView month1, month2, month3, sumfare1, sumfare2, sumfare3;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_nav_driverhistory, container, false);


        database=FirebaseDatabase.getInstance();
        reference=database.getReference("History");
        user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getUid();
        month1=(TextView)v.findViewById(R.id.thismonth);
        month2=(TextView)v.findViewById(R.id.lastmonth);
        month3=(TextView)v.findViewById(R.id.monthbefore);
        sumfare1=(TextView)v.findViewById(R.id.firstvalue);
        sumfare2=(TextView)v.findViewById(R.id.secondvalue);
        sumfare3=(TextView)v.findViewById(R.id.thirdvalue);


        Calendar cal=Calendar.getInstance();
        String today=new SimpleDateFormat("MMM").format(cal.getTime());
        String stoday=new SimpleDateFormat("MM").format(cal.getTime());
        int x=Integer.parseInt(stoday);
        x--;
        stoday=Integer.toString(x);
        month1.setText(today);

        cal.add(Calendar.MONTH,-1);
        String past=new SimpleDateFormat("MMM").format(cal.getTime());
        String spast=new SimpleDateFormat("MM").format(cal.getTime());
        int y=Integer.parseInt(spast);
        y--;
        spast=Integer.toString(y);
        month2.setText(past);

        cal.add(Calendar.MONTH,-1);
        String past2=new SimpleDateFormat("MMM").format(cal.getTime());
        String spast2=new SimpleDateFormat("MM").format(cal.getTime());
        int z=Integer.parseInt(spast2);
        z--;
        spast2=Integer.toString(z);
        month3.setText(past2);

        driverRef = FirebaseDatabase.getInstance().getReference().child("Drivers").child("S7qswgzCNadNN7z3vn8FfKutdTB3").child("History").child(spast2);
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


        driverRef = FirebaseDatabase.getInstance().getReference().child("Drivers").child("S7qswgzCNadNN7z3vn8FfKutdTB3").child("History").child(spast);
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

        driverRef = FirebaseDatabase.getInstance().getReference().child("Drivers").child("S7qswgzCNadNN7z3vn8FfKutdTB3").child("History").child(stoday);
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
        return v;
    }
}
