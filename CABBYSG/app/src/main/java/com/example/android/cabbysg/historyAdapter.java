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

public class historyAdapter extends ArrayAdapter<historyDetails> {

    public historyAdapter(Context context, ArrayList<historyDetails> details){
        super(context,0, details);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        final historyDetails historydetails = getItem(position);

        if(convertView ==null){
            convertView= LayoutInflater.from(getContext()).inflate(R.layout.history_item,parent,false);
        }
        final TextView name1= convertView.findViewById(R.id.history2Name);
        TextView date1= convertView.findViewById(R.id.history2Date);
        TextView fare1= convertView.findViewById(R.id.history2Fare);
        TextView time1= convertView.findViewById(R.id.history2Time);
        final TextView vehNb1= convertView.findViewById(R.id.history2CarPlate);
        final TextView vehModel1= convertView.findViewById(R.id.history2CarModel);
        final TextView driverRating1=convertView.findViewById(R.id.history2Rating);
        TextView route1=convertView.findViewById(R.id.history2Route);
        TextView from1=convertView.findViewById(R.id.history2From);
        TextView to1=convertView.findViewById(R.id.history2To);
        ImageView blackBox= convertView.findViewById(R.id.history2BlackBox);
        final de.hdodenhof.circleimageview.CircleImageView history2Profile = convertView.findViewById(R.id.history2Profile);
        //history2Profile (profile picture of driver)

        DatabaseReference driver_db = FirebaseDatabase.getInstance().getReference().child("Drivers").child(historydetails.driverID);
        driver_db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                name1.setText(dataSnapshot.child("firstName").getValue().toString() + " " + dataSnapshot.child("lastName").getValue().toString());
                vehNb1.setText(dataSnapshot.child("regNum").getValue().toString());
                vehModel1.setText(dataSnapshot.child("model").getValue().toString());
                driverRating1.setText(dataSnapshot.child("rating").getValue().toString());
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