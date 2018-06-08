package com.example.android.cabbysg;


import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.app.ProgressDialog.STYLE_SPINNER;


public class startpage extends AppCompatActivity {

    public void riderLogin(View view){
        startActivity(new Intent(this,riderLogin.class));
    }

    public void driverLogin(View view){
        startActivity(new Intent(this,driverLogin.class));
    }

    FirebaseAuth mAuth;
    //Create progress dialog
    ProgressDialog pd;
    //Database reference
    DatabaseReference rider_db = FirebaseDatabase.getInstance().getReference("Rider");
    DatabaseReference driver_db = FirebaseDatabase.getInstance().getReference("Drivers");
    //User
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startpage);

        //Init pd
        pd = new ProgressDialog(startpage.this, STYLE_SPINNER);
        //Set cancelable to not let users click it away
        pd.setCancelable(false);
        //Set message and show
        pd.setMessage("Please Wait...");

        mAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        if(user!=null){
            pd.show();
            rider_db.child(user.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Intent intent = new Intent(startpage.this, MenuBar.class);
                    startActivity(intent);
                    pd.dismiss();
                    finish();
                    return;
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            driver_db.child(user.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Intent intent = new Intent(startpage.this, driver_menubar.class);
                    startActivity(intent);
                    pd.dismiss();
                    finish();
                    return;
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            /*if(rider_db.child(user.getUid()).getKey()!=null) {
                Intent intent = new Intent(startpage.this, driver_menubar.class);
                startActivity(intent);
                finish();
                return;
            }else if(driver_db.child(user.getUid()).getKey()!=null){
                Intent intent = new Intent(startpage.this, driver_menubar.class);
                startActivity(intent);
                finish();
                return;
            }*/
        }
    }

   /* @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }*/
}
