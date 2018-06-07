package com.example.android.cabbysg;


import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class startpage extends AppCompatActivity {

    public void riderLogin(View view){
        startActivity(new Intent(this,riderLogin.class));
    }

    public void driverLogin(View view){
        startActivity(new Intent(this,driverLogin.class));
    }

    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener firebaseAuthListener;
    DatabaseReference rider_db = FirebaseDatabase.getInstance().getReference("Rider");
    DatabaseReference driver_db = FirebaseDatabase.getInstance().getReference("Drivers");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startpage);

        mAuth = FirebaseAuth.getInstance();
        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if(user!=null){
                    if(rider_db.child(user.getUid()).getKey().equals(user.getUid())) {
                        Intent intent = new Intent(startpage.this, MenuBar.class);
                        startActivity(intent);
                        finish();
                        return;
                    }else if(driver_db.child(user.getUid()).getKey().equals(user.getUid())){
                        Intent intent = new Intent(startpage.this, driver_menubar.class);
                        startActivity(intent);
                        finish();
                        return;
                    }
                }
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(firebaseAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(firebaseAuthListener);
    }
}
