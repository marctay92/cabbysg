package com.example.android.cabbysg;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.app.ProgressDialog.STYLE_SPINNER;


public class nav_profile extends Fragment implements View.OnClickListener {

    Button editProfile, changePw, logOut;
    TextView firstNameView,lastNameView,mobileView,emailView;
    de.hdodenhof.circleimageview.CircleImageView profilePic;

    DatabaseReference current_user_db;

    FirebaseAuth mAuth;
    FirebaseUser user;

    //Create Progress Dialog
    ProgressDialog pd;

    public nav_profile() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_nav_profile, container, false);

        //User Authentication
        mAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        current_user_db = FirebaseDatabase.getInstance().getReference("Rider");

        //Init pd
        pd = new ProgressDialog(getActivity(), STYLE_SPINNER);
        //Set cancelable to not let users click it away
        pd.setCancelable(false);
        //Set message and show
        pd.setMessage("Please Wait...");


        //Text Views
        firstNameView= rootView.findViewById(R.id.firstName);
        lastNameView= rootView.findViewById(R.id.lastName);
        mobileView= rootView.findViewById(R.id.mobileNb);
        emailView= rootView.findViewById(R.id.email);

        //Image Views
        profilePic = rootView.findViewById(R.id.profile_image);

        //Buttons
        editProfile = rootView.findViewById(R.id.editProfileBtn);
        changePw = rootView.findViewById(R.id.changePwBtn);
        logOut = rootView.findViewById(R.id.logOutBtn);

        editProfile.setOnClickListener(this);
        changePw.setOnClickListener(this);
        logOut.setOnClickListener(this);

        //extract TextViews from database MARCUS
        current_user_db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                    if(user.getUid().equals(postSnapshot.getKey())) {
                        UserInfo uInfo = postSnapshot.getValue(UserInfo.class);
                        firstNameView.setText(uInfo.getFirstName());
                        lastNameView.setText(uInfo.getLastName());
                        mobileView.setText(uInfo.getMobileNum());
                        emailView.setText(uInfo.getEmail());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return rootView;
    }

    @Override
    public void onClick(View view) {
        pd.show();
        Fragment fragment = null;
        switch (view.getId()) {
            case R.id.editProfileBtn:
                fragment = new editProfile();
                replaceFragment(fragment);
                pd.dismiss();
                break;

            case R.id.changePwBtn:
                fragment = new editPassword();
                replaceFragment(fragment);
                pd.dismiss();
                break;

            case R.id.logOutBtn:
                FirebaseAuth.getInstance().signOut();
                moveToNewActivity();
                break;

        }
    }

    @Override
    public void onStart() {
        super.onStart();
        //mAuth.addAuthStateListener(firebaseAuthListener);
        FirebaseUser user = mAuth.getCurrentUser();
        if(user==null){
            moveToNewActivity();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    public void replaceFragment(Fragment somefragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, somefragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void moveToNewActivity() {
        Intent i = new Intent(getActivity(), startpage.class);
        startActivity(i);
        getActivity().overridePendingTransition(0,0);
        pd.dismiss();
    }
}


