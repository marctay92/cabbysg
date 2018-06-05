package com.example.android.cabbysg;


import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class schedule_adaptor extends ArrayAdapter<schedule_details> {
    public schedule_adaptor(Context context, ArrayList<schedule_details> users){
        super(context,0,users);
    }

    @Override
    public View getView (int position, View convertView, ViewGroup parent) {

        schedule_details user = getItem(position);

        if (convertView == null){
            convertView= LayoutInflater.from(getContext()).inflate(R.layout.schedule_template,parent,false);
        }

        TextView s_date = (TextView) convertView.findViewById(R.id.s_date);
        TextView s_time = (TextView) convertView.findViewById(R.id.s_time);
        TextView s_name = (TextView) convertView.findViewById(R.id.s_name);
        TextView s_carplatenumber = (TextView) convertView.findViewById(R.id.s_carplatenumber);
        TextView s_rating = (TextView) convertView.findViewById(R.id.s_rating);
        TextView s_destination = (TextView) convertView.findViewById(R.id.s_destination);
        TextView s_location = (TextView) convertView.findViewById(R.id.s_location);
        TextView s_type = (TextView) convertView.findViewById(R.id.s_type);
        TextView s_price = (TextView) convertView.findViewById(R.id.s_price);
        ImageView s_cancel=(ImageView)convertView.findViewById(R.id.s_cancel);



        s_date.setText(user.schedule_date);
        s_time.setText(user.schedule_time);
        s_name.setText(user.schedule_name);
        s_carplatenumber.setText(user.schedule_carplatenumber);
        s_rating.setText(user.schedule_rating);
        s_destination.setText(user.schedule_destination);
        s_location.setText(user.schedule_location);
        s_type.setText(user.schedule_type);
        s_price.setText(user.schedule_price);

        s_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //   int position = (Integer) view.getTag();
                //  schedule_details user = getItem(position);

                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("Confirm Cancel Booking?");
                builder.setMessage("Booking will be cancelled");

                /*MARCUS SEE THIS*/                    builder.setPositiveButton("Continue", null/*set to delete from database*/);
                builder.setNegativeButton("Exit",null);

                AlertDialog dialog=builder.create();
                dialog.show();

            }
        });

        return convertView;





    }


}
