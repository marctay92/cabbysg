package com.example.android.cabbysg;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.ContentValues.TAG;


public class editPassword extends Fragment {

    EditText currentPasswordText, newPasswordText, reEnterText;
    Button submitBtn;

    public editPassword() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View rootView = inflater.inflate(R.layout.fragment_edit_password, container, false);

       currentPasswordText= rootView.findViewById(R.id.currentPassword);
       newPasswordText= rootView.findViewById(R.id.newPassword);
       reEnterText= rootView.findViewById(R.id.reEnter);
       submitBtn= rootView.findViewById(R.id.confirmChange);

       //On click listener
       submitBtn.setOnClickListener(new View.OnClickListener(){
           public void onClick(View view){
               //Conversion to string
               String currentPasswordStr = currentPasswordText.getText().toString();
               String newPasswordStr = newPasswordText.getText().toString();
               String reEnterStr = reEnterText.getText().toString();
               boolean validNewPassword = false;
               boolean validReEnter=false;
               boolean validCurrent=false;

               //Condition to check
               if (TextUtils.isEmpty(newPasswordStr)) {
                   newPasswordText.setError("Please enter your password");
               } else if (!(isValidPassword(newPasswordStr))){
                   newPasswordText.setError("Min 8 characters with at least 1 Upper and 1 number");
               } else validNewPassword = true;

               if (TextUtils.isEmpty(reEnterStr)) {
                   reEnterText.setError("Please enter your password");
               } else if (!(TextUtils.equals(newPasswordStr,reEnterStr))) {
                   reEnterText.setError("Password does not match");
                   newPasswordText.setError("Password does not match");
               } else validReEnter = true;

//>               MARCUS PLEASE DO THE CHECK FOR CURRENT PASSWORD

               if(validNewPassword&&validReEnter&&validCurrent){
                   FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                   String newPassword = "SOME-SECURE-PASSWORD";

                   user.updatePassword(newPassword)
                           .addOnCompleteListener(new OnCompleteListener<Void>() {
                               @Override
                               public void onComplete(@NonNull Task<Void> task) {
                                   if (task.isSuccessful()) {
                                       Log.d(TAG, "User password updated.");
                                   }
                               }
                           });
                   Fragment newFragment=new nav_profile();
                   FragmentTransaction transaction = getFragmentManager().beginTransaction();
                   transaction.replace(R.id.editPasswordFragment,newFragment);
                   transaction.addToBackStack(null);
                   transaction.commit();
               }
           }
       });

       return rootView;
    }

    //Valid Password
    public boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;

        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}$";

        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();
    }
}