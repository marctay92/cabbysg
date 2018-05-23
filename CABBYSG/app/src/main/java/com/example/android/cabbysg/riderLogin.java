package com.example.android.cabbysg;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class riderLogin extends AppCompatActivity {

    EditText riderNb;
    EditText riderPassword;
    Button riderLoginButton;

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
        startActivity(new Intent(this,register.class));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rider_login);
        riderNb = findViewById(R.id.riderNb);
        riderPassword  = findViewById(R.id.riderPassword);
        riderLoginButton = findViewById(R.id.riderLoginButton);

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
