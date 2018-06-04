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
        TextView name1=(TextView)convertView.findViewById(R.id.name1);
        TextView date1=(TextView)convertView.findViewById(R.id.date1);
        TextView fare1=(TextView)convertView.findViewById(R.id.fare1);
        TextView time1=(TextView)convertView.findViewById(R.id.time1);
        TextView vehNb1=(TextView)convertView.findViewById(R.id.vehNb1);
        ImageView blackBox=(ImageView)convertView.findViewById(R.id.black_rectangle_box1);

        name1.setText(historydetails.historyDriver);
        date1.setText(historydetails.historyDate);
        fare1.setText(historydetails.historyPrice);
        time1.setText(historydetails.historyTime);
        vehNb1.setText(historydetails.historyVeh);

        //setOnclickListener(new View.OnClickListener)
        blackBox.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick (View view){
                //int position=(Integer)view.getTag();
                //historyDetails historydetails=getItem(position);

                /*Intent intent=new Intent(getActivity(),nav_history2.class);
                startActivity(intent);*/
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
        });
        return convertView;
    }
}
