package com.example.android.cabbysg;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class register extends AppCompatActivity {
    EditText firstName;
    EditText lastName;
    static EditText dob;
    EditText email;
    EditText mobileNb;
    EditText password;
    EditText rePassword;
    ImageView profilePic;
    Context ctx = this;

    public void onReg (View view){
        String str_firstName = firstName.getText().toString();
        String str_lastName = lastName.getText().toString();
        String str_dob = dob.getText().toString();
        String str_email = email.getText().toString();
        String str_mobileNb = mobileNb.getText().toString();
        String str_password = password.getText().toString();
        String str_rePassword = rePassword.getText().toString();

        boolean validFirstName = false;
        boolean validLastName = false;
        boolean validDob = false;
        boolean validEmail = false;
        boolean validNum = false;
        boolean validPW = false;
        boolean validRePW = false;

        if(TextUtils.isEmpty(str_firstName)) {
            firstName.setError("Please enter your first name");
        } else validFirstName = true;

        if (TextUtils.isEmpty(str_lastName)) {
            lastName.setError("Please enter your last name");
        } else validLastName = true;

        if (TextUtils.isEmpty(str_dob)) {
            dob.setError("Please enter your date of birth");
        } else validDob = true;

        if (TextUtils.isEmpty(str_email)) {
            email.setError("Please enter your email");
        //} else if (){

        } else validEmail = true;

        if (TextUtils.isEmpty(str_mobileNb)) {
            mobileNb.setError("Please enter your mobile number");
        } else validNum = true;

        if (TextUtils.isEmpty(str_password)) {
            password.setError("Please enter your password");
        } else if (!(isValidPassword(str_password))){
            password.setError("Min 8 characters with at least 1 Upper and 1 number");
        } else validPW = true;

        if (TextUtils.isEmpty(str_rePassword)) {
            rePassword.setError("Please enter your password");
        } else if (!(TextUtils.equals(str_password,str_rePassword))) {
            rePassword.setError("Password does not match");
            password.setError("Password does not match");
        } else validRePW = true;

        if (validFirstName && validLastName && validDob && validEmail && validNum && validPW && validRePW){
            String type = "register";
            AccCreateBackgroundWorker backgroundWorker = new AccCreateBackgroundWorker(this, new VolleyCallback() {
                @Override
                public void onSuccess(String result) {

                }

                @Override
                public void onSuccess2(Bundle result) {

                }

                @Override
                public void onSuccessLogin(String userID) {
                    //Insert intent to homepage for rider
                    System.out.println("R#" + userID);
                }

                @Override
                public void onFailLogin() {

                }

                @Override
                public void onFailRegister(String remarks) {
                    Toast.makeText(ctx,remarks,Toast.LENGTH_LONG).show();
                }
            });
            backgroundWorker.execute(type,str_firstName,str_lastName,str_dob,str_email,str_mobileNb,str_password);
        }
    }
    public boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;

        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}$";

        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

    }
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
        profilePic = findViewById(R.id.profilePic);
        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(v);
            }
        });
    }
    public static class DatePickerFragment extends DialogFragment implements
            DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            dob.setText(year + "-" + (month+1) + "-" + day);
        }
    }
    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

}
