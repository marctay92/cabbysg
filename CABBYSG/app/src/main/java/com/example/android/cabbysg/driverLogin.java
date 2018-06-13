package com.example.android.cabbysg;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
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

import static android.app.ProgressDialog.STYLE_SPINNER;


public class driverLogin extends AppCompatActivity {

    private EditText driverEmail, driverPassword;
    private Button driverLoginButton;


    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener firebaseAuthListener;
    //DatabaseReference db = FirebaseDatabase.getInstance().getReference();
    //Create progress dialog
    ProgressDialog pd;
    DatabaseReference rider_db = FirebaseDatabase.getInstance().getReference().child("Rider");
    DatabaseReference driver_db = FirebaseDatabase.getInstance().getReference().child("Drivers");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_login);
        //Init pd
        pd = new ProgressDialog(driverLogin.this, STYLE_SPINNER);
        //Set cancelable to not let users click it away
        pd.setCancelable(false);
        //Set message and show
        pd.setMessage("Please Wait...");

        driverEmail = findViewById(R.id.driverEmail);
        driverPassword = findViewById(R.id.driverPassword);

        driverLoginButton = findViewById(R.id.driverLoginButton);

        mAuth = FirebaseAuth.getInstance();
        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = mAuth.getCurrentUser();
                if(user!=null){
                    if(driver_db.child(user.getUid()).getKey()!= null) {
                        Intent intent = new Intent(driverLogin.this, driver_menubar.class);
                        startActivity(intent);
                        pd.dismiss();
                        finish();
                        return;
                    }else if(rider_db.child(user.getUid()).getKey()!= null){
                        Toast.makeText(driverLogin.this,"Login to wrong page.",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(driverLogin.this, startpage.class);
                        startActivity(intent);
                        pd.dismiss();
                        finish();
                        return;
                    }
                }else{
                    pd.dismiss();
                }
            }
        };

        driverLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd.show();
                String email = driverEmail.getText().toString();
                String password = driverPassword.getText().toString();

                boolean validEmail = false, validPassword = false;

                if(TextUtils.isEmpty(email)) {
                    driverEmail.setError("Please enter your number");
                }else validEmail = true;

                if (TextUtils.isEmpty(password)) {
                    driverPassword.setError("Please enter your password");
                }else validPassword = true;

                if(validEmail && validPassword) {
                    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(driverLogin.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                System.out.println("Driver not Logged in");
                                Toast.makeText(driverLogin.this, "Sign In Error", Toast.LENGTH_SHORT).show();
                                pd.dismiss();
                            }
                        }
                    });
                }else{
                    pd.dismiss();
                }
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
        pd.dismiss();
    }

}
