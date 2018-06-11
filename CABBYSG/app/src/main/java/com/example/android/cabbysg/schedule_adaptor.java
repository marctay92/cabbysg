package com.example.android.cabbysg;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class schedule_adaptor extends ArrayAdapter<schedule_details> {
    public schedule_adaptor(Context context, ArrayList<schedule_details> users){
        super(context,0,users);
    }

    @Override
    public View getView (int position, View convertView, ViewGroup parent) {

        final schedule_details scheduleDetails = getItem(position);
        final String user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final DatabaseReference scheduledRidesDatabase = FirebaseDatabase.getInstance().getReference().child("scheduledRides").child(scheduleDetails.schedule_ID);
        final DatabaseReference riderScheduleDatabase = FirebaseDatabase.getInstance().getReference().child("Rider").child(user_id).child("scheduledRides").child(scheduleDetails.schedule_ID);
        final DatabaseReference driverScheduleDatabase = FirebaseDatabase.getInstance().getReference().child("Drivers").child(scheduleDetails.schedule_driverID).child("scheduledRides").child(scheduleDetails.schedule_ID);

        if (convertView == null){
            convertView= LayoutInflater.from(getContext()).inflate(R.layout.schedule_template,parent,false);
        }

        TextView s_date = convertView.findViewById(R.id.s_date);
        TextView s_time = convertView.findViewById(R.id.s_time);
        TextView s_name = convertView.findViewById(R.id.s_name);
        TextView s_carplatenumber = convertView.findViewById(R.id.s_carplatenumber);
        TextView s_rating = convertView.findViewById(R.id.s_rating);
        TextView s_destination = convertView.findViewById(R.id.s_destination);
        TextView s_location = convertView.findViewById(R.id.s_location);
        TextView s_type = convertView.findViewById(R.id.s_type);
        TextView s_price = convertView.findViewById(R.id.s_price);
        de.hdodenhof.circleimageview.CircleImageView profilePic = convertView.findViewById(R.id.s_profile);
        ImageView s_cancel= convertView.findViewById(R.id.s_cancel);

        s_date.setText(scheduleDetails.schedule_date);
        s_time.setText(scheduleDetails.schedule_time);
        s_name.setText(scheduleDetails.schedule_driverName);
        s_carplatenumber.setText(scheduleDetails.schedule_carplatenumber);
        s_rating.setText(scheduleDetails.schedule_rating);
        s_destination.setText(scheduleDetails.schedule_destination);
        s_location.setText(scheduleDetails.schedule_location);
        s_type.setText(scheduleDetails.schedule_routeType);
        s_price.setText(scheduleDetails.schedule_fare);

        if(!scheduleDetails.schedule_pic.equals("")){
            Glide.with(getContext()).load(scheduleDetails.schedule_pic).into(profilePic);
        }

        s_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //   int position = (Integer) view.getTag();
                //  schedule_details user = getItem(position);

                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("Confirm Cancel Booking?");
                builder.setMessage("Booking will be cancelled");

                /*MARCUS SEE THIS*/
                builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        scheduledRidesDatabase.removeValue();
                        driverScheduleDatabase.removeValue();
                        riderScheduleDatabase.removeValue();
                    }
                });
                builder.setNegativeButton("Exit",null);

                AlertDialog dialog=builder.create();
                dialog.show();

            }
        });

        return convertView;





    }


}
