package com.example.android.cabbysg;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class editProfile extends Fragment{

    EditText firstNameEditText, lastNameEditText, emailEditText, mobileEditText;
    Button deleteAccBtn, saveChangesBtn;

    FirebaseAuth mAuth;


    public editProfile() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View rootView = inflater.inflate(R.layout.fragment_edit_profile, container, false);

       //Edit Text fields
       firstNameEditText = rootView.findViewById(R.id.firstNameEdit);
       lastNameEditText = rootView.findViewById(R.id.lastNameEdit);
       mobileEditText = rootView.findViewById(R.id.mobileNbEdit);
       emailEditText = rootView.findViewById(R.id.emailEdit);

       /*String user_id = mAuth.getCurrentUser().getUid();
       DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference().child("Rider").child(user_id);

       current_user_db.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {



            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

       //firstNameEditText.setText();*/

        //MARCUS PLEASE TAKE THE INFO FROM DATABASE HERE!!!!

       //Button fields
       deleteAccBtn = rootView.findViewById(R.id.deleteAcc);
       saveChangesBtn = rootView.findViewById(R.id.saveEdit);

       deleteAccBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               AlertDialog.Builder builder=new AlertDialog.Builder(v.getContext());
               builder.setTitle("Confirm Account Deletion?");
               builder.setMessage("Account Deletion is irreversible and all information will be lost");

/**/               builder.setPositiveButton("Continue", null/*set to delete from database*/);
               builder.setNegativeButton("Cancel",null);

               AlertDialog dialog=builder.create();
               dialog.show();
           }
       });

       //saveChangesBtn.setOnClickListener(--->"MARCUS PLEASE SEND THE CHANGES TO THE DATABASE HERE!!!");
        saveChangesBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                //Conversion to string
                String firstNameStr = firstNameEditText.getText().toString();
                String lastNameStr = lastNameEditText.getText().toString();
                String mobileStr = mobileEditText.getText().toString();
                String emailStr = emailEditText.getText().toString();
                boolean validEditFirst = false;
                boolean validEditLast=false;
                boolean validEditMobile=false;
                boolean validEditEmail=false;

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

//>             if(validEditFirst&&validEditLast&&validEditEmail&&validEditMobile){Save to database}
            }
        });

       return rootView;
    }



}
