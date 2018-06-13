package com.example.android.cabbysg;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
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

import static android.app.ProgressDialog.STYLE_SPINNER;


public class riderLogin extends AppCompatActivity {

    private EditText riderEmail, riderPassword;
    private Button riderLoginButton;


    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener firebaseAuthListener;
    DatabaseReference rider_db = FirebaseDatabase.getInstance().getReference("Rider");
    DatabaseReference driver_db = FirebaseDatabase.getInstance().getReference("Drivers");
    //Create progress dialog
    ProgressDialog pd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rider_login);
        //Init pd
        pd = new ProgressDialog(riderLogin.this, STYLE_SPINNER);
        //Set cancelable to not let users click it away
        pd.setCancelable(false);
        //Set message and show
        pd.setMessage("Please Wait...");

        riderEmail = findViewById(R.id.riderEmail);
        riderPassword  = findViewById(R.id.riderPassword);

        riderLoginButton = findViewById(R.id.riderLoginButton);

        mAuth = FirebaseAuth.getInstance();
        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = mAuth.getCurrentUser();
                if(user!=null){
                    if(rider_db.child(user.getUid()).getKey().equals(user.getUid())) {
                        Intent intent = new Intent(riderLogin.this, MenuBar.class);
                        startActivity(intent);
                        pd.dismiss();
                        finish();
                        return;
                    }else if(driver_db.child(user.getUid()).getKey().equals(user.getUid())){
                        Toast.makeText(riderLogin.this,"Login to wrong page.",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(riderLogin.this, startpage.class);
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

        riderLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd.show();
                String email = riderEmail.getText().toString();
                String password = riderPassword.getText().toString();

                boolean validEmail = false, validPassword = false;

                if(TextUtils.isEmpty(email)) {
                    riderEmail.setError("Please enter your number");
                }else validEmail = true;

                if (TextUtils.isEmpty(password)) {
                    riderPassword.setError("Please enter your password");
                }else validPassword = true;

                if(validEmail && validPassword) {
                    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(riderLogin.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(riderLogin.this, "Sign In Error", Toast.LENGTH_SHORT).show();
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

    public void navRegister(View view){
        startActivity(new Intent(this,riderRegister.class));
    }

    /*public void dismissKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (null != activity.getCurrentFocus())
            imm.hideSoftInputFromWindow(activity.getCurrentFocus()
                    .getApplicationWindowToken(), 0);
    }*/

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
