package com.example.android.cabbysg;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class riderLogin extends AppCompatActivity {

    EditText riderNb;
    EditText riderPassword;
    Context ctx = this;

    public void dismissKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (null != activity.getCurrentFocus())
            imm.hideSoftInputFromWindow(activity.getCurrentFocus()
                    .getApplicationWindowToken(), 0);
    }

    public void OnRiderLogin (View view){
        dismissKeyboard(this);
        String mnumber = riderNb.getText().toString();
        String password = riderPassword.getText().toString();

        boolean validNum = false;
        boolean validPW = false;

        if(TextUtils.isEmpty(mnumber)) {
            riderNb.setError("Please enter your number");
        }else validNum = true;

        if (TextUtils.isEmpty(password)) {
            riderPassword.setError("Please enter your password");
        }else validPW = true;

        if(validNum && validPW){
            String type = "RiderLogin";
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
                    //Insert intent to homepage for rider
                    /* Intent i = new Intent(ctx,RiderHome.class); */
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
    public void navRegister(View view){
        startActivity(new Intent(this,register.class));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rider_login);
        riderNb = findViewById(R.id.riderNb);
        riderPassword  = findViewById(R.id.riderPassword);
    }

    /*public void showDialog (){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Please enter username ...");

        // Set up the input
        final EditText input = new EditText(this);
        // Specify the type of input expected
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Put function after clicking OK
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                //Put function after clicking cancel
                dialog.cancel();
            }
        });

        builder.show();
    }*/
}
