package com.example.android.cabbysg;

import android.app.Fragment;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class historyAdapter extends ArrayAdapter<historyDetails> {
    private Context appContext;

    public historyAdapter(Context context, ArrayList<historyDetails> details){
        super(context,0, details);
        appContext = context;

    }

    @Override
    public View getView(int position, View convertView,ViewGroup parent){
        historyDetails historydetails = getItem(position);

        if(convertView ==null){
            convertView= LayoutInflater.from(getContext()).inflate(R.layout.history_item,parent,false);
        }
        TextView name1= convertView.findViewById(R.id.history2Name);
        TextView date1= convertView.findViewById(R.id.history2Date);
        TextView fare1= convertView.findViewById(R.id.history2Fare);
        TextView time1= convertView.findViewById(R.id.history2Time);
        TextView vehNb1= convertView.findViewById(R.id.history2CarPlate);
        TextView vehModel1= convertView.findViewById(R.id.history2CarModel);
        TextView driverRating1=convertView.findViewById(R.id.history2Rating);
        TextView route1=convertView.findViewById(R.id.history2Route);
        TextView from1=convertView.findViewById(R.id.history2From);
        TextView to1=convertView.findViewById(R.id.history2To);
        ImageView blackBox= convertView.findViewById(R.id.history2BlackBox);
        //history2Profile (profile picture of driver)

        String fullName = historydetails.getFirstName() + " " + historydetails.getLastName();

        name1.setText(fullName);
        date1.setText(historydetails.tripDate);
        fare1.setText(historydetails.fare);
        time1.setText(historydetails.tripTime);
        vehNb1.setText(historydetails.regNum);
        vehModel1.setText(historydetails.model);
        driverRating1.setText(historydetails.rating);
        route1.setText(historydetails.selectedRoute);
        from1.setText(historydetails.pickUpLocation);
        to1.setText(historydetails.destination);
/*
        //setOnclickListener(new View.OnClickListener)
        blackBox.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick (View view){
                //int position=(Integer)view.getTag();
                //historyDetails historydetails=getItem(position);

                /*Intent intent=new Intent(getActivity(),nav_history2.class);
                startActivity(intent);
                nav_history fragment=null;
                fragment=new nav_history();
                nav_history2 someFragment= null;
                someFragment=new nav_history2();
                //FragmentTransaction transaction=((Activity)context).getFragmentManager().beginTransaction();
                android.support.v4.app.FragmentManager manager = ((AppCompatActivity)appContext).getSupportFragmentManager();
                android.support.v4.app.FragmentTransaction transaction=manager.beginTransaction();
                transaction.replace(R.id.nav_history, someFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        }); */

        return convertView;
    }
}