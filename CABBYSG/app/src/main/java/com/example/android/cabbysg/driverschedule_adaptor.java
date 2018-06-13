package com.example.android.cabbysg;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class driverschedule_adaptor extends ArrayAdapter<driverschedule_details> {
    DatabaseReference scheduledRidesDatabase,riderScheduleDatabase,driverScheduleDatabase;

    public driverschedule_adaptor(Context context, ArrayList<driverschedule_details> users){
        super(context,0,users);
    }

    @Override
    public View getView (int position, View convertView, ViewGroup parent) {

        final driverschedule_details driverschedule_details = getItem(position);
        final String user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        scheduledRidesDatabase = FirebaseDatabase.getInstance().getReference().child("scheduledRides").child(driverschedule_details.driverschedule_id);
        riderScheduleDatabase = FirebaseDatabase.getInstance().getReference().child("Rider").child(driverschedule_details.driverschedule_riderID);//.child("scheduledRides").child(driverschedule_details.driverschedule_id);
        //driverScheduleDatabase = FirebaseDatabase.getInstance().getReference().child("Drivers").child(user_id).child("scheduledRides").child(driverschedule_details.driverschedule_id);

        if (convertView == null){
            convertView= LayoutInflater.from(getContext()).inflate(R.layout.driverschedule_template,parent,false);
        }

        TextView ds_date = convertView.findViewById(R.id.ds_date);
        //TextView ds_time = convertView.findViewById(R.id.ds_time);
        final TextView ds_name = convertView.findViewById(R.id.ds_name);
        final TextView ds_rating = convertView.findViewById(R.id.ds_rating);
        TextView ds_destination = convertView.findViewById(R.id.ds_destination);
        TextView ds_location = convertView.findViewById(R.id.ds_location);
        TextView ds_type = convertView.findViewById(R.id.ds_type);
        TextView ds_price = convertView.findViewById(R.id.ds_price);
        final de.hdodenhof.circleimageview.CircleImageView profilePic = convertView.findViewById(R.id.ds_profile);
        ImageView ds_cancel= convertView.findViewById(R.id.ds_cancel);

        DatabaseReference rider_db = FirebaseDatabase.getInstance().getReference().child("Rider").child(driverschedule_details.driverschedule_riderID);
        rider_db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ds_name.setText(dataSnapshot.child("firstName").getValue().toString() + " " + dataSnapshot.child("lastName").getValue().toString());
                Double driverRating = Double.parseDouble(dataSnapshot.child("rating").getValue().toString());
                DecimalFormat df2 = new DecimalFormat(".#");
                ds_rating.setText(df2.format(driverRating));
                if(dataSnapshot.child("profileImageUrl").exists()){
                    Glide.with(getContext()).load(dataSnapshot.child("profileImageUrl").getValue().toString()).into(profilePic);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        ds_date.setText(driverschedule_details.driverschedule_date);
        ds_destination.setText(driverschedule_details.driverschedule_destination);
        ds_location.setText(driverschedule_details.driverschedule_location);
        ds_type.setText(driverschedule_details.driverschedule_type);
        ds_price.setText(driverschedule_details.driverschedule_price);

        ds_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //   int position = (Integer) view.getTag();
                //  schedule_details user = getItem(position);

                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("Confirm Cancel Booking?");
                builder.setMessage("Booking will be cancelled");

                builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        scheduledRidesDatabase.removeValue();
                        //driverScheduleDatabase.removeValue();
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
