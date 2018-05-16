package com.example.android.cabbysg;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;


public class startpage extends AppCompatActivity {

    public void riderLogin(View view){
        startActivity(new Intent(this,riderLogin.class));
    }

    public void driverLogin(View view){
        startActivity(new Intent(this,driverLogin.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startpage);
    }
}
