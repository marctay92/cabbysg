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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.ContentValues.TAG;


public class editPassword extends Fragment {

    EditText currentPasswordText, newPasswordText, reEnterText;
    Button submitBtn;
    boolean validCurrent,validNewPassword,validReEnter;

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
               final String currentPasswordStr = currentPasswordText.getText().toString();
               String newPasswordStr = newPasswordText.getText().toString();
               String reEnterStr = reEnterText.getText().toString();
               validNewPassword = false;
               validReEnter=false;
               validCurrent=false;

               //Condition to check
               FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
               AuthCredential credential = EmailAuthProvider
                                            .getCredential(user.getEmail(), currentPasswordStr);
                // Prompt the user to re-provide their sign-in credentials
               user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                   @Override
                   public void onComplete(@NonNull Task<Void> task) {
                       if (!task.isSuccessful()){
                           currentPasswordText.setError("Password incorrect");
                       }else {
                           validCurrent = true;
                       }
                   }
               });

               //Check new password
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


               if(validNewPassword&&validReEnter&&validCurrent){
                   user.updatePassword(newPasswordStr)
                           .addOnCompleteListener(new OnCompleteListener<Void>() {
                               @Override
                               public void onComplete(@NonNull Task<Void> task) {
                                   if (task.isSuccessful()) {
                                       Fragment newFragment=new nav_profile();
                                       FragmentTransaction transaction = getFragmentManager().beginTransaction();
                                       transaction.replace(R.id.editPasswordFragment,newFragment);
                                       transaction.addToBackStack(null);
                                       transaction.commit();
                                   }else {
                                       Toast.makeText(getContext(),"Unable to update password. Try Again.",Toast.LENGTH_SHORT).show();
                                   }
                               }
                           });
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