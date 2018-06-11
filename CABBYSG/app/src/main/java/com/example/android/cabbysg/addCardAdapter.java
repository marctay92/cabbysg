package com.example.android.cabbysg;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
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
        TextView cardNumber = convertView.findViewById(R.id.cardNumber);
        ImageView cardImageType = convertView.findViewById(R.id.cardImageType);

        String cardDetails = details.cardDetailsNumber;
        String mask = cardDetails.replaceAll("\\w(?=\\w{4})", "*");

        if (details.cardType.equals("MASTER_CARD")){
            cardImageType.setImageResource(R.drawable.mastercard);
        }
        if (details.cardType.equals("VISA_CARD")){
            cardImageType.setImageResource(R.drawable.visa);
        }

        cardNumber.setText(mask);
        return convertView;
    }
}
