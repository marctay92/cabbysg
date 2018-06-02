package com.example.android.cabbysg;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class historyAdapter extends ArrayAdapter<historyDetails> {
    public historyAdapter(Context context, ArrayList<historyDetails> details){
        super(context,0, details);
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
        TextView vehNb1=(TextView)convertView.findViewById(R.id.vehNb1);
        //create imageView button for the onClick
        //setTag(?)

        name1.setText(historydetails.historyDriver);
        date1.setText(historydetails.historyDate);
        fare1.setText(historydetails.historyPrice);
        vehNb1.setText(historydetails.historyVeh);

        //setOnclickListener(new View.OnClickListener)

        return convertView;
    }
}
