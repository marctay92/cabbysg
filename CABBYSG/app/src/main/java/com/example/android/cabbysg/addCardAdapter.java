package com.example.android.cabbysg;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class addCardAdapter extends ArrayAdapter<cardDetailsContainer> {
    public addCardAdapter(Context context, ArrayList<cardDetailsContainer>cardDetails){
        super(context,0,cardDetails);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        cardDetailsContainer details =getItem(position);

        if(convertView==null){
            convertView= LayoutInflater.from(getContext()).inflate(R.layout.paymentlayout,parent,false);
        }
        TextView cardNumber = (TextView)convertView.findViewById(R.id.cardNumber);

        cardNumber.setText(details.cardDetailsNumber);
        return convertView;
    }
}
