package com.example.android.cabbysg;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;


public class register extends AppCompatActivity {
    EditText firstName, lastName, dob, email, mobileNb, password, rePassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        dob = findViewById(R.id.dob);
        email = findViewById(R.id.email);
        mobileNb = findViewById(R.id.mobileNb);
        password = findViewById(R.id.password);
        rePassword = findViewById(R.id.rePassword);
    }
    public void onReg (View view){
        String str_firstName = firstName.getText().toString();
        String str_lastName = lastName.getText().toString();
        String str_dob = dob.getText().toString();
        String str_email = email.getText().toString();
        String str_mobileNb = mobileNb.getText().toString();
        String str_password = password.getText().toString();
        String str_rePassword = rePassword.getText().toString();

        if(TextUtils.isEmpty(str_firstName)) {
            firstName.setError("Please enter your first name");
        }
        else if (TextUtils.isEmpty(str_lastName)) {
            lastName.setError("Please enter your last name");
        }
        else if (TextUtils.isEmpty(str_dob)) {
            dob.setError("Please enter your date of birth");
        }
        else if (TextUtils.isEmpty(str_email)) {
            email.setError("Please enter your email");
        }
        else if (TextUtils.isEmpty(str_mobileNb)) {
            mobileNb.setError("Please enter your mobile number");
        }
        else if (TextUtils.isEmpty(str_password)) {
            password.setError("Please enter your password");
        }
        else if (TextUtils.isEmpty(str_rePassword)) {
            rePassword.setError("Please enter your password");
        }
        else if (!(TextUtils.equals(str_password,str_rePassword))) {
            rePassword.setError("Password does not match");
            password.setError("Password does not match");
        }
        else{
            String type = "register";
            BackgroundWorker backgroundWorker = new BackgroundWorker(this);
            backgroundWorker.execute(type,str_firstName,str_lastName,str_dob,str_email,str_mobileNb,str_password);
        }
    }
}
