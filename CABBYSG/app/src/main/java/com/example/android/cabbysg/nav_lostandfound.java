package com.example.android.cabbysg;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.app.ProgressDialog.STYLE_SPINNER;


/**
 * A simple {@link Fragment} subclass.
 */
public class nav_lostandfound extends Fragment {
    EditText itemDescription, lostPhoneNum, lostEmail;
    String itemDescriptionStr = "", lostPhoneNumStr = "",lostEmailStr ="";
    Button lostItemSubmit;
    boolean validDescription = false, validPhoneNum = false, validEmail = false;
    DatabaseReference current_user_db,lost_item_db;
    //Create progress dialog
    ProgressDialog pd;

    FirebaseUser user;


    public nav_lostandfound() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_nav_lostandfound, container, false);

        //Init pd
        pd = new ProgressDialog(getContext(), STYLE_SPINNER);
        //Set cancelable to not let users click it away
        pd.setCancelable(false);
        //Set message and show
        pd.setMessage("Please Wait...");

        //Init Edit Text
        itemDescription = rootView.findViewById(R.id.itemDescription);
        lostPhoneNum = rootView.findViewById(R.id.lostPhoneNumber);
        lostEmail = rootView.findViewById(R.id.lostEmail);
        lostItemSubmit = rootView.findViewById(R.id.lostItemSubmit);

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
                //Extract String from EditText
                itemDescriptionStr = itemDescription.getText().toString();
                lostPhoneNumStr = lostPhoneNum.getText().toString();
                lostEmailStr = lostEmail.getText().toString();

                //Checks
                if(TextUtils.isEmpty(itemDescriptionStr)) {
                    itemDescription.setError("Please enter your item description");
                } else validDescription = true;

                if (TextUtils.isEmpty(lostPhoneNumStr)) {
                    lostPhoneNum.setError("Please enter your phone number");
                }else if (!TextUtils.isDigitsOnly(lostPhoneNumStr)){
                    lostPhoneNum.setError("Please enter only numbers");
                }else if(lostPhoneNumStr.length() > 8 || lostPhoneNumStr.length()<8){
                    lostPhoneNum.setError("Please enter only 8 digits");
                }else validPhoneNum = true;

                if (TextUtils.isEmpty(lostEmailStr)) {
                    lostEmail.setError("Please enter your email");
                }else if (!isEmailValid(lostEmailStr)){
                    lostEmail.setError("Please enter a valid email");
                }else validEmail = true;

                if(validDescription&&validPhoneNum&&validEmail){
                    pd.show();
                    Map<String, Object> newPost = new HashMap<>();
                    newPost.put("itemDescription", itemDescriptionStr);
                    newPost.put("PhoneNum", lostPhoneNumStr);
                    newPost.put("Email", lostEmailStr);
                    newPost.put("riderID",user.getUid());

                    current_user_db.child(user.getUid()).child("LostItem").child(lost_item_db.getKey()).setValue(true);
                    lost_item_db.updateChildren(newPost);
                    Toast.makeText(getContext(),"Lost Item Report submitted.",Toast.LENGTH_SHORT);
                    Fragment newFragment=new nav_home();
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.riderLostandFound,newFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                    pd.dismiss();

                }
            }
        });

        return rootView;
    }
    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
    public static boolean isNameValid(String name){
        String expression = "^[a-zA-Z]{4,}(?: [a-zA-Z]+){0,2}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(name);
        return matcher.matches();
    }

}
