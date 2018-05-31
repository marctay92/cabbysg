package com.example.android.cabbysg;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;

public class nav_profile extends Fragment implements View.OnClickListener {

    Button editProfile;
    Button changePw;
    Button logOut;
    TextView firstNameView;
    TextView lastNameView;
    TextView mobileView;
    TextView emailView;

    int counter;

    public nav_profile() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_nav_profile, container, false);

        //Text Views
        firstNameView=(TextView)rootView.findViewById(R.id.firstName);
        lastNameView=(TextView)rootView.findViewById(R.id.lastName);
        mobileView=(TextView)rootView.findViewById(R.id.mobileNb);
        emailView=(TextView)rootView.findViewById(R.id.email);

        //extract TextViews from database MARCUS

        //Buttons
        editProfile = (Button) rootView.findViewById(R.id.editProfileBtn);
        changePw = (Button) rootView.findViewById(R.id.changePwBtn);
        logOut = (Button) rootView.findViewById(R.id.logOutBtn);

        counter=0;

        editProfile.setOnClickListener(this);
        changePw.setOnClickListener(this);
        logOut.setOnClickListener(this);

        return rootView;
    }

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
                            counter=0;
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
                            counter=0;
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
/*logOut
            case R.id.logOutBtn:
                SharedPreferences myPrefs = getSharedPreferences("Activity",
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

    public void replaceFragment(Fragment somefragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.navProfile, somefragment);
        transaction.addToBackStack(null);
        editProfile.setVisibility(View.GONE);
        changePw.setVisibility(View.GONE);
        logOut.setVisibility(View.GONE);
        transaction.commit();
    }

    /*logout
    private void setLoginState(boolean status) {
        SharedPreferences sp = getSharedPreferences("LoginState",
                MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putBoolean("setLoggingOut", status);
        ed.commit();
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


