package com.example.android.cabbysg;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;

public class nav_profile extends Fragment implements View.OnClickListener {

    Button editProfile, changePw, logOut;
    TextView firstNameView,lastNameView,mobileView,emailView;

    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener firebaseAuthListener;

    int counter;

    public nav_profile() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_nav_profile, container, false);

        //User Authentication
        mAuth = FirebaseAuth.getInstance();
        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if(user==null){
                    //moveToNewActivity();
                }
            }
        };

        //Text Views
        firstNameView= rootView.findViewById(R.id.firstName);
        lastNameView= rootView.findViewById(R.id.lastName);
        mobileView= rootView.findViewById(R.id.mobileNb);
        emailView= rootView.findViewById(R.id.email);

        //extract TextViews from database MARCUS

        //Buttons
        editProfile = rootView.findViewById(R.id.editProfileBtn);
        changePw = rootView.findViewById(R.id.changePwBtn);
        logOut = rootView.findViewById(R.id.logOutBtn);

        counter=0;

        editProfile.setOnClickListener(this);
        changePw.setOnClickListener(this);
        logOut.setOnClickListener(this);

        return rootView;
    }
/*
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        if (counter == 1){
                            Fragment newFragment=new nav_profile();
                            FragmentTransaction transaction = getFragmentManager().beginTransaction();
                            transaction.replace(R.id.editProfileFragment,newFragment);
                            transaction.addToBackStack(null);
                            transaction.commit();
                            editProfile.setVisibility(View.VISIBLE);
                            changePw.setVisibility(View.VISIBLE);
                            logOut.setVisibility(View.VISIBLE);
                        }
                        else if(counter==2){
                            Fragment newFragment=new nav_profile();
                            FragmentTransaction transaction = getFragmentManager().beginTransaction();
                            transaction.replace(R.id.editPasswordFragment,newFragment);
                            transaction.addToBackStack(null);
                            transaction.commit();
                            editProfile.setVisibility(View.VISIBLE);
                            changePw.setVisibility(View.VISIBLE);
                            logOut.setVisibility(View.VISIBLE);
                        }
                        else{

                        }
                    }
                    return true;
                }
                return false;
            }
        });
    }
*/

    @Override
    public void onClick(View view) {
        Fragment fragment = null;
        switch (view.getId()) {
            case R.id.editProfileBtn:
                fragment = new editProfile();
                replaceFragment(fragment);
                counter = 1;
                break;

            case R.id.changePwBtn:
                fragment = new editPassword();
                replaceFragment(fragment);
                counter = 2;
                break;

            case R.id.logOutBtn:
                FirebaseAuth.getInstance().signOut();
                break;
                /*SharedPreferences myPrefs = getSharedPreferences("Activity",
                        MODE_PRIVATE);
                SharedPreferences.Editor editor = myPrefs.edit();
                editor.clear();
                editor.commit();
                //AppState.getSingleInstance().setLoggingOut(true);
                setLoginState(true);
                Log.d(TAG, "Now log out and start the activity login");
                Intent intent = new Intent(nav_profile.this,
                        LoginPage.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);*/
        }
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

    public void replaceFragment(Fragment somefragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, somefragment);
        transaction.addToBackStack(null);
        //editProfile.setVisibility(View.GONE);
        //changePw.setVisibility(View.GONE);
       // logOut.setVisibility(View.GONE);
        transaction.commit();
    }

    /*private void moveToNewActivity() {
        Intent i = new Intent(getActivity(), startpage.class);
        startActivity(i);
        getActivity().overridePendingTransition(0,0);

    }*/



    /** I believe this is the code use to extract information from the database
     * however your friend uses multiple "background workers" to achieve this

     @Override public void displayFieldDetails(JsonObject obj) {
     name = findViewById(R.id.edit_name);
     name = findViewById(R.id.edit_name);
     phoneNo = findViewById(R.id.edit_phoneNo);
     email = findViewById(R.id.edit_email);
     }**/

}


