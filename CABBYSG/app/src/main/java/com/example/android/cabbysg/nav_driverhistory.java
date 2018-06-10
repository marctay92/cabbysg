package com.example.android.cabbysg;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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

    BarChart barChart;
    ArrayList<String> month;
    Random random;
    ArrayList<BarEntry>barEntries;

    FirebaseDatabase database;
    DatabaseReference reference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_nav_driverhistory, container, false);

        barChart=(BarChart)v.findViewById(R.id.barChart);
        database=FirebaseDatabase.getInstance();
        reference=database.getReference("History");

        random=new Random();

        Calendar cal=Calendar.getInstance();
        String today=new SimpleDateFormat("MMM").format(cal.getTime());
        Log.d("TODAY: ", today);

        Calendar calen=Calendar.getInstance();
        calen.add(Calendar.MONTH,-2);
        String past=new SimpleDateFormat("MMM").format(calen.getTime());
        Log.d("PAST", past);


        createRandomBarGraph(past,today);
        /*List<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0f, 30f));
        entries.add(new BarEntry(1f, 80f));
        entries.add(new BarEntry(2f, 60f));
        entries.add(new BarEntry(3f, 50f));
        // gap of 2f
        entries.add(new BarEntry(5f, 70f));
        entries.add(new BarEntry(6f, 60f));

        BarDataSet set = new BarDataSet(entries, "BarDataSet");

        BarData data = new BarData(set);
        data.setBarWidth(0.9f); // set custom bar width
        barChart.setData(data);
        barChart.setFitBars(true); // make the x-axis fit exactly all bars
        barChart.invalidate(); // refresh
        */

        return v;
    }

    //creates the graph
    public void createRandomBarGraph(String Date1,String Date2){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM");

        try {
            Date date1 = simpleDateFormat.parse(Date1);
            Date date2=simpleDateFormat.parse(Date2);

            Calendar mDate1=Calendar.getInstance();
            Calendar mDate2=Calendar.getInstance();

            mDate1.clear();
            mDate2.clear();

            mDate1.setTime(date1);
            mDate2.setTime(date2);

            month =new ArrayList<>();
            month=getList(mDate1, mDate2);
            barEntries=new ArrayList<>();
            float max=0f;
            float value=0f;
            for(int j=0;j<month.size();j++){
                max=100f;
                value= random.nextFloat()*max;
                barEntries.add(new BarEntry(value,j));
            }

        }catch(ParseException e){
            e.printStackTrace();
        }
        BarDataSet barDataSet=new BarDataSet(barEntries,"Dates");
        BarData barData=new BarData(barDataSet);
        barChart.setData(barData);
        //arChart.setDescription;
    }

    public ArrayList<String> getList(Calendar startDate, Calendar endDate){
        ArrayList<String>list=new ArrayList<>();
        while (startDate.compareTo(endDate)<=0){
            list.add(getDate(startDate));
            startDate.add(Calendar.MONTH,1);
        }
        return list;
    }

    public String getDate(Calendar cld){
        String curDate=(cld.get(Calendar.MONTH)+1)+"/"
                +cld.get(Calendar.DAY_OF_MONTH);
        try{
            Date date=new SimpleDateFormat("MMM").parse(curDate);
            curDate=new SimpleDateFormat("MMM").format(date);
        }catch(ParseException e){
            e.printStackTrace();
        }
        return curDate;
    }
/*    @Override
    public void onStart() {
        super.onStart();

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //DataPoint[]dp=new DataPoint[dataSnapshot.getChildrenCount()];
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }*/
}
