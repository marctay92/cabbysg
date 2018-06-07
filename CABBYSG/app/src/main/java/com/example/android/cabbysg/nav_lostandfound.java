package com.example.android.cabbysg;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class nav_lostandfound extends Fragment {
    EditText itemDescription, lostPhoneNum, lostEmail;
    String itemDescriptionStr, lostPhoneNumStr,lostEmailStr;
    Button lostItemSubmit;
    boolean validDescription = false, validPhoneNum = false, validEmail = false;
    DatabaseReference current_user_db,lost_item_db;

    FirebaseUser user;


    public nav_lostandfound() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_nav_lostandfound, container, false);

        //Init Edit Text
        itemDescription = rootView.findViewById(R.id.itemDescription);
        lostPhoneNum = rootView.findViewById(R.id.lostPhoneNumber);
        lostEmail = rootView.findViewById(R.id.lostEmail);
        lostItemSubmit = rootView.findViewById(R.id.lostItemSubmit);

        //Extract String from EditText
        itemDescriptionStr = itemDescription.getText().toString();
        lostPhoneNumStr = lostPhoneNum.getText().toString();
        lostEmailStr = lostEmail.getText().toString();

        user = FirebaseAuth.getInstance().getCurrentUser();
        current_user_db = FirebaseDatabase.getInstance().getReference().child("Rider");
        lost_item_db = FirebaseDatabase.getInstance().getReference().child("LostItem").push();

        //extract TextViews from database
        current_user_db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                    if(user.getUid().equals(postSnapshot.getKey())) {
                        UserInfo uInfo = postSnapshot.getValue(UserInfo.class);
                        lostPhoneNum.setText(uInfo.getMobileNum());
                        lostEmail.setText(uInfo.getEmail());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        lostItemSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(itemDescriptionStr)) {
                    itemDescription.setError("Please enter your item description");
                } else validDescription = true;

                if (TextUtils.isEmpty(lostPhoneNumStr)) {
                    lostPhoneNum.setError("Please enter your last name");
                } else validPhoneNum = true;

                if (TextUtils.isEmpty(lostEmailStr)) {
                    lostEmail.setError("Please enter your email");
                } else validEmail = true;

                if(validDescription&&validPhoneNum&&validEmail){
                    Map newPost = new HashMap();
                    //newPost.put("email",str_email);
                    newPost.put("itemDescription", itemDescriptionStr);
                    newPost.put("PhoneNum", lostPhoneNumStr);
                    newPost.put("Email", lostEmailStr);
                    newPost.put("riderID",user.getUid());

                    lost_item_db.setValue(newPost);
                }
            }
        });

        return rootView;
    }


}
