package com.example.android.cabbysg;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class riderRegister extends AppCompatActivity {
    EditText firstName, lastName, email, mobileNb, password, rePassword;
    ImageView profilePic;
    Button registerButton;

    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener firebaseAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if(user!=null){
                    Intent intent = new Intent(riderRegister.this, MenuBar.class);
                    startActivity(intent);
                    finish();
                    return;
                }
            }
        };

        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        email = findViewById(R.id.email);
        mobileNb = findViewById(R.id.mobileNb);
        password = findViewById(R.id.password);
        rePassword = findViewById(R.id.rePassword);
        profilePic = findViewById(R.id.profilePic);

        registerButton = findViewById(R.id.registerButton);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String str_firstName = firstName.getText().toString();
                final String str_lastName = lastName.getText().toString();
                final String str_email = email.getText().toString();
                final String str_mobileNb = mobileNb.getText().toString();
                final String str_password = password.getText().toString();
                final String str_rePassword = rePassword.getText().toString();

                boolean validFirstName = false;
                boolean validLastName = false;
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

                if (validFirstName && validLastName && validEmail && validNum && validPW && validRePW){
                    mAuth.createUserWithEmailAndPassword(str_email,str_password).addOnCompleteListener(riderRegister.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                Toast.makeText(riderRegister.this, "sign up error", Toast.LENGTH_SHORT).show();
                            }else{
                                String user_id = mAuth.getCurrentUser().getUid();
                                DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference().child("Rider").child(user_id);

                                Map newPost = new HashMap();
                                //newPost.put("email",str_email);
                                newPost.put("firstName",str_firstName);
                                newPost.put("lastName",str_lastName);
                                newPost.put("mobileNum",str_mobileNb);
                                newPost.put("email",mAuth.getCurrentUser().getEmail());

                                current_user_db.setValue(newPost).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            Toast.makeText(riderRegister.this,"Registration is successful",Toast.LENGTH_SHORT);
                                        }else{
                                            Toast.makeText(riderRegister.this,"Registration is unsuccessful",Toast.LENGTH_SHORT);
                                        }
                                    }
                                });
                            }
                        }
                    });
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
    }

    public boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;

        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}$";

        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

    }
    
}
