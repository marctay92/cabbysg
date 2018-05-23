package com.example.android.cabbysg;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


public class driverLogin extends AppCompatActivity {

    EditText driverNb;
    EditText driverPassword;

    public void onDriverLogin (View view){
        String mnumber = driverNb.getText().toString();
        String password = driverPassword.getText().toString();

        if(TextUtils.isEmpty(mnumber)) {
            driverNb.setError("Please enter your number");
        }
        else if (TextUtils.isEmpty(password)) {
            driverPassword.setError("Please enter your password");
        }
        else{
            String type = "DriverLogin";
            BackgroundWorker backgroundWorker = new BackgroundWorker(this);
            backgroundWorker.execute(type,mnumber,password);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_login);
        driverNb = findViewById(R.id.driverNum);
        driverPassword  = findViewById(R.id.driverPassword);

    }
}
