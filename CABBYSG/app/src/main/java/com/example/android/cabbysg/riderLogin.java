package com.joshuakwek.cabbysg;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;


public class riderLogin extends AppCompatActivity {

    riderNb= findViewById(R.id.riderNb);
    riderPassword= findViewById(R.id.riderPassword);

    public void OnRiderLogin (View view){
        String mnumber = riderNb.getText.toString();
        String password = riderPassword.getText.toString();
        String type = "RiderLogin";
        BackgroundWorker backgroundWorker = new BackgroundWorker(this);
        backgroundWorker.execute(type,mnumber,password);

    }
    public void navRegister(View view){
        startActivity(new Intent(riderLogin.this,register.class));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rider_login);
    }
}
