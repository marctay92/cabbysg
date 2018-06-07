package com.example.android.cabbysg;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.cabbysg.R;
import com.example.android.cabbysg.UserInfo;
import com.example.android.cabbysg.nav_profile;
import com.example.android.cabbysg.startpage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import static android.app.ProgressDialog.STYLE_SPINNER;

public class driverEditProfile extends Fragment {

    EditText firstNameEditText, lastNameEditText, emailEditText, mobileEditText;
    TextView carPlateTextView,carModelTextView;
    String firstNameStr, lastNameStr, mobileStr, emailStr, carPlateStr, carModelStr;
    boolean validEditFirst = false;
    boolean validEditLast=false;
    boolean validEditMobile=false;
    boolean validEditEmail=false;
    boolean saveChanges = false;
    Button deleteAccBtn, saveChangesBtn;

    //Create Progress Dialog
    ProgressDialog pd;

    DatabaseReference current_user_db;

    FirebaseAuth mAuth;
    FirebaseUser user;
    //FirebaseAuth.AuthStateListener firebaseAuthListener;

    Map newPost = new HashMap();

    public driverEditProfile() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_driver_edit_profile, container, false);
        //Init pd
        pd = new ProgressDialog(getActivity(), STYLE_SPINNER);
        //Set cancelable to not let users click it away
        pd.setCancelable(false);
        //Set message and show
        pd.setMessage("Please Wait...");

        //Edit Text fields
        firstNameEditText = rootView.findViewById(R.id.dFirstNameEdit);
        lastNameEditText = rootView.findViewById(R.id.dLastNameEdit);
        mobileEditText = rootView.findViewById(R.id.dMobileNbEdit);
        emailEditText = rootView.findViewById(R.id.dEmailEdit);
        carModelTextView = rootView.findViewById(R.id.dRegNum);
        carPlateTextView = rootView.findViewById(R.id.dModel);


        //User Authentication
        mAuth = FirebaseAuth.getInstance();

        user = FirebaseAuth.getInstance().getCurrentUser();

        current_user_db = FirebaseDatabase.getInstance().getReference("Drivers");

        //extract TextViews from database
        current_user_db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                    if(user.getUid().equals(postSnapshot.getKey())) {
                        DriverInfo dInfo = postSnapshot.getValue(DriverInfo.class);
                        firstNameEditText.setText(dInfo.firstName);
                        lastNameEditText.setText(dInfo.lastName);
                        mobileEditText.setText(dInfo.mobileNum);
                        emailEditText.setText(dInfo.email);
                        carPlateTextView.setText(dInfo.model);
                        carModelTextView.setText(dInfo.regNum);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //Button fields
        deleteAccBtn = rootView.findViewById(R.id.dDeleteAcc);
        saveChangesBtn = rootView.findViewById(R.id.dSaveEdit);

        deleteAccBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(v.getContext());
                builder.setTitle("Confirm Account Deletion?");
                builder.setMessage("Account Deletion is irreversible and all information will be lost");

                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch(which){
                            case DialogInterface.BUTTON_POSITIVE:
                                showDialog();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                // User clicked the No button
                                break;
                        }
                    }
                };

                builder.setPositiveButton("Continue", dialogClickListener);
                builder.setNegativeButton("Cancel",dialogClickListener);

                AlertDialog dialog=builder.create();
                dialog.show();
            }
        });

        //saveChangesBtn.setOnClickListener(--->"MARCUS PLEASE SEND THE CHANGES TO THE DATABASE HERE!!!");
        saveChangesBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                //Conversion to string
                firstNameStr = firstNameEditText.getText().toString();
                lastNameStr = lastNameEditText.getText().toString();
                mobileStr = mobileEditText.getText().toString();
                emailStr = emailEditText.getText().toString();
                carModelStr = carModelTextView.getText().toString();
                carPlateStr = carPlateTextView.getText().toString();


                //set condition to run reauthentication with the right function
                saveChanges = true;

                //Condition to check
                if(TextUtils.isEmpty(firstNameStr)) {
                    firstNameEditText.setError("Please enter your first name");
                } else validEditFirst = true;

                if (TextUtils.isEmpty(lastNameStr)) {
                    lastNameEditText.setError("Please enter your last name");
                } else validEditLast = true;

                if (TextUtils.isEmpty(emailStr)) {
                    emailEditText.setError("Please enter your email");
                } else validEditEmail = true;

                if (TextUtils.isEmpty(mobileStr)) {
                    mobileEditText.setError("Please enter your mobile number");
                } else validEditMobile = true;

                if(validEditFirst&&validEditLast&&validEditEmail&&validEditMobile){
                    pd.show();
                    user.updateEmail(emailStr).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                String user_id = mAuth.getCurrentUser().getUid();
                                DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference().child("Drivers").child(user_id);

                                Map newPost = new HashMap();
                                //newPost.put("email",str_email);
                                newPost.put("firstName", firstNameStr);
                                newPost.put("lastName", lastNameStr);
                                newPost.put("mobileNum", mobileStr);
                                newPost.put("email",emailStr);
                                newPost.put("regNum",carPlateStr);
                                newPost.put("model",carModelStr);

                                current_user_db.setValue(newPost);

                                Fragment newFragment=new nav_profile();
                                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                                transaction.replace(R.id.editProfileFragment,newFragment);
                                transaction.addToBackStack(null);
                                transaction.commit();
                                pd.dismiss();

                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            if (e instanceof FirebaseAuthRecentLoginRequiredException){
                                showDialog();
                                pd.dismiss();
                            }
                        }
                    });
                }
            }
        });
        return rootView;
    }
    private void moveToNewActivity() {
        Intent i = new Intent(getActivity(), startpage.class);
        startActivity(i);
        getActivity().overridePendingTransition(0,0);
        pd.dismiss();
    }

    private void showDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Re-key in your credentials");

        // Set up the input
        final String email = user.getEmail();
        final EditText password = new EditText(getActivity());
        // Specify the type of input expected
        password.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        password.setHint("Please Enter Your Password");
        password.setHintTextColor(getResources().getColor(R.color.text_color_primary));
        builder.setView(password);

        // Set up the buttons
        builder.setPositiveButton("Re-login", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Put function after clicking OK
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                // Get auth credentials from the user for re-authentication. The example below shows
                // email and password credentials but there are multiple possible providers,
                // such as GoogleAuthProvider or FacebookAuthProvider.
                AuthCredential credential = EmailAuthProvider
                        .getCredential(email, password.getText().toString());

                // Prompt the user to re-provide their sign-in credentials
                user.reauthenticate(credential)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    if (saveChanges) {
                                        System.out.println("User re-authenticated.");
                                        String user_id = mAuth.getCurrentUser().getUid();
                                        DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference().child("Drivers").child(user_id);

                                        Map newPost = new HashMap();
                                        newPost.put("firstName", firstNameStr);
                                        newPost.put("lastName", lastNameStr);
                                        newPost.put("mobileNum", mobileStr);
                                        newPost.put("email",emailStr);
                                        newPost.put("regNum",carPlateStr);
                                        newPost.put("model",carModelStr);

                                        current_user_db.setValue(newPost);

                                        Fragment newFragment = new nav_driverprofile();
                                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                                        transaction.replace(R.id.driverEditProfile, newFragment);
                                        transaction.addToBackStack(null);
                                        transaction.commit();
                                    }else{
                                        // User clicked the Continue button
                                        user.delete()
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            pd.show();
                                                            Toast.makeText(getActivity(),"User account deleted",Toast.LENGTH_LONG).show();
                                                            current_user_db.removeValue();
                                                            moveToNewActivity();
                                                        }
                                                    }
                                                });
                                    }
                                }
                            }
                        });
            }
        });

        builder.show();
    }

    /*@Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(firebaseAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(firebaseAuthListener);
    }*/
}
