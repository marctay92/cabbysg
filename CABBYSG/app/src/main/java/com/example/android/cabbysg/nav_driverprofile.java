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

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

import static android.app.ProgressDialog.STYLE_SPINNER;


public class nav_driverprofile extends Fragment implements View.OnClickListener {

    Button dEditProfile, dChangePw, dLogOut;
    TextView dFirstNameView,dLastNameView,dMobileView,dEmailView, dCarPlateView, dCarModelView;
    String firstNameStr, lastNameStr, mobileStr, emailStr,profileUrlStr ="";
    de.hdodenhof.circleimageview.CircleImageView profilePic;

    DatabaseReference current_user_db;

    FirebaseAuth mAuth;
    FirebaseUser user;

    //Create Progress Dialog
    ProgressDialog pd;

    public nav_driverprofile() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_nav_driverprofile, container, false);

        //User Authentication
        mAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        current_user_db = FirebaseDatabase.getInstance().getReference("Drivers").child(user.getUid());

        //Init pd
        pd = new ProgressDialog(getActivity(), STYLE_SPINNER);
        //Set cancelable to not let users click it away
        pd.setCancelable(false);
        //Set message and show
        pd.setMessage("Please Wait...");

        //Text Views
        dFirstNameView= rootView.findViewById(R.id.dFirstName);
        dLastNameView= rootView.findViewById(R.id.dLastName);
        dMobileView= rootView.findViewById(R.id.dMobileNb);
        dEmailView= rootView.findViewById(R.id.dEmail);
        dCarPlateView=rootView.findViewById(R.id.dCarPlate);
        dCarModelView=rootView.findViewById(R.id.dCarModel);
        profilePic = rootView.findViewById(R.id.dprofile_image);

        //Buttons
        dEditProfile = rootView.findViewById(R.id.dEditProfileBtn);
        dChangePw = rootView.findViewById(R.id.dChangePwBtn);
        dLogOut = rootView.findViewById(R.id.dLogOutBtn);

        dEditProfile.setOnClickListener(this);
        dChangePw.setOnClickListener(this);
        dLogOut.setOnClickListener(this);

        //extract TextViews from database MARCUS
        current_user_db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists() && dataSnapshot.getChildrenCount()>0) {
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                    if(map.get("firstName")!=null){
                        firstNameStr = map.get("firstName").toString();
                        dFirstNameView.setText(firstNameStr);
                    }
                    if(map.get("lastName")!=null){
                        lastNameStr = map.get("lastName").toString();
                        dLastNameView.setText(lastNameStr);
                    }
                    if(map.get("mobileNum")!=null){
                        mobileStr = map.get("mobileNum").toString();
                        dMobileView.setText(mobileStr);
                    }
                    if(map.get("email")!=null){
                        emailStr = map.get("email").toString();
                        dEmailView.setText(emailStr);
                    }
                    if(map.get("profileImageUrl")!=null){
                        profileUrlStr = map.get("profileImageUrl").toString();
                        Glide.with(getContext()).load(profileUrlStr).into(profilePic);
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
            case R.id.dEditProfileBtn:
                fragment = new driverEditProfile();
                replaceFragment(fragment);
                pd.dismiss();
                break;

            case R.id.dChangePwBtn:
                fragment = new driverEditPassword();
                replaceFragment(fragment);
                pd.dismiss();
                break;

            case R.id.dLogOutBtn:
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
        transaction.replace(R.id.driverProfile, somefragment);
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
