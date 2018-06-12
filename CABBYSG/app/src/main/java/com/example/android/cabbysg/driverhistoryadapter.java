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
import java.util.ArrayList;

public class driverhistoryadapter extends ArrayAdapter<driverhistoryitem>{

    public driverhistoryadapter(Context context, ArrayList<driverhistoryitem> details){
        super(context,0, details);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        final driverhistoryitem historydetails = getItem(position);

        if(convertView ==null){
            convertView= LayoutInflater.from(getContext()).inflate(R.layout.driverhistorytemplate,parent,false);
        }
        final TextView name1= convertView.findViewById(R.id.history2Name);
        TextView date1= convertView.findViewById(R.id.history2Date);
        TextView fare1= convertView.findViewById(R.id.history2Fare);
        TextView time1= convertView.findViewById(R.id.history2Time);
        final TextView riderRating1=convertView.findViewById(R.id.history2Rating);
        TextView route1=convertView.findViewById(R.id.history2Route);
        TextView from1=convertView.findViewById(R.id.history2From);
        TextView to1=convertView.findViewById(R.id.history2To);
        ImageView blackBox= convertView.findViewById(R.id.history2BlackBox);
        final de.hdodenhof.circleimageview.CircleImageView history2Profile = convertView.findViewById(R.id.history2Profile);
        //history2Profile (profile picture of driver)
        DatabaseReference rider_db = FirebaseDatabase.getInstance().getReference().child("Rider").child(historydetails.riderID);
        rider_db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                name1.setText(dataSnapshot.child("firstName").getValue().toString() + " " + dataSnapshot.child("lastName").getValue().toString());
                riderRating1.setText(dataSnapshot.child("rating").getValue().toString());
                if(dataSnapshot.child("profileImageUrl").exists()){
                    Glide.with(getContext()).load(dataSnapshot.child("profileImageUrl").getValue().toString()).into(history2Profile);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        date1.setText(historydetails.tripDate);
        fare1.setText(historydetails.fare);
        time1.setText(historydetails.tripTime);
        route1.setText(historydetails.selectedRoute);
        from1.setText(historydetails.pickUpLocation);
        to1.setText(historydetails.destination);
        return convertView;
    }
}
