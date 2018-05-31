package com.example.android.cabbysg;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class riderLogin extends AppCompatActivity {

    private EditText riderEmail, riderPassword;
    private Button riderLoginButton, riderRegister;

    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener firebaseAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rider_login);

        riderEmail = findViewById(R.id.riderEmail);
        riderPassword  = findViewById(R.id.riderPassword);

        riderLoginButton = findViewById(R.id.riderLoginButton);
        riderRegister = findViewById(R.id.riderRegister);

        mAuth = FirebaseAuth.getInstance();
        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if(user!=null){
                    Intent intent = new Intent(riderLogin.this, MenuBar.class);
                    startActivity(intent);
                    finish();
                    return;
                }
            }
        };

        riderRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = riderEmail.getText().toString();
                String password = riderPassword.getText().toString();
                mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(riderLogin.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful()){
                            Toast.makeText(riderLogin.this, "sign up error", Toast.LENGTH_SHORT).show();
                        }else{
                            String user_id = mAuth.getCurrentUser().getUid();
                            DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference().child("Rider").child(user_id);
                            current_user_db.setValue(true);
                        }
                    }
                });
            }
        });

        riderLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = riderEmail.getText().toString();
                String password = riderPassword.getText().toString();
                mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(riderLogin.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful()){
                            Toast.makeText(riderLogin.this, "sign in error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
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

    /*public void dismissKeyboard(Activity activity) {
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
                 * *
                @Override
                public void onSuccess(String result) {

                }

                @Override
                public void onSuccess2(Bundle result) {

                }

                @Override
                public void onSuccessLogin(String userID) {
                    //Insert intent to homepage for rider
                    /* Intent i = new Intent(ctx,RiderHome.class);
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
    }*/
    /*public void navRegister(View view){
        startActivity(new Intent(this,register.class));
    }
    */
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

    /*protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        SharedPreferences sp = getSharedPreferences("LoginState",
                MODE_PRIVATE);
        boolean stateValue  = sp.getBoolean("setLoggingOut", false);
        if (requestCode == MAIN_ACTIVITY_REQUEST_CODE) {

            if (!stateValue) {
                finish();
            } else {
                //AppState.getSingleInstance().setLoggingOut(false);
                updateLoginState(false);
                super.onActivityResult(requestCode, resultCode, data);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }*/
}
