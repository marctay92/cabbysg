package com.example.android.cabbysg;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;


public class riderLogin extends AppCompatActivity {

    EditText riderNb;
    EditText riderPassword;

    public void OnRiderLogin (View view){
        String mnumber = riderNb.getText().toString();
        String password = riderPassword.getText().toString();

        if(TextUtils.isEmpty(mnumber)) {
            riderNb.setError("Please enter your number");
        }
        else if (TextUtils.isEmpty(password)) {
            riderPassword.setError("Please enter your password");
        }
        else{
            String type = "RiderLogin";
            BackgroundWorker backgroundWorker = new BackgroundWorker(this);
            backgroundWorker.execute(type,mnumber,password);
        }
    }
    public void navRegister(View view){
        startActivity(new Intent(riderLogin.this,register.class));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rider_login);
        riderNb = findViewById(R.id.riderNb);
        riderPassword  = findViewById(R.id.riderPassword);

    }
}
