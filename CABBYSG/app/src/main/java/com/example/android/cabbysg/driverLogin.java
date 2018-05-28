package com.example.android.cabbysg;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;


public class driverLogin extends AppCompatActivity {

    EditText driverNb;
    EditText driverPassword;
    Context ctx = this;

    public void onDriverLogin (View view){
        String mnumber = driverNb.getText().toString();
        String password = driverPassword.getText().toString();

        boolean validNum = false;
        boolean validPW = false;

        if(TextUtils.isEmpty(mnumber)) {
            driverNb.setError("Please enter your number");
        }else validNum = true;

        if (TextUtils.isEmpty(password)) {
            driverPassword.setError("Please enter your password");
        }else validPW = true;

        if(validNum && validPW) {
            String type = "DriverLogin";
            AccCreateBackgroundWorker backgroundWorker = new AccCreateBackgroundWorker(this, new VolleyCallback() {
                /**This is the callback class, when you create this class you have to implement the method
                 * but it can be left empty, and only the method you need can be implemented
                 * **/
                @Override
                public void onSuccess(String result) {

                }

                @Override
                public void onSuccess2(Bundle result) {

                }

                @Override
                public void onSuccessLogin(String userID) {
                    //Insert intent to homepage for driver
                    System.out.println(userID);
                }

                @Override
                public void onFailLogin() {
                    Toast.makeText(ctx,"Incorrect Credentials",Toast.LENGTH_LONG).show();
                }

                @Override
                public void onFailRegister(String remarks) {

                }
            });
            backgroundWorker.execute(type, mnumber, password);
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
