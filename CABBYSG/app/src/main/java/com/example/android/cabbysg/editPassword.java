package com.example.android.cabbysg;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.app.ProgressDialog.STYLE_SPINNER;
import static android.content.ContentValues.TAG;


public class editPassword extends Fragment {

    EditText currentPasswordText, newPasswordText, reEnterText;
    String newPasswordStr,reEnterStr;
    Button submitBtn;
    boolean validCurrent,validNewPassword,validReEnter;
    FirebaseUser user;
    String email;

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
       user = FirebaseAuth.getInstance().getCurrentUser();
       email = user.getEmail();

       //On click listener
       submitBtn.setOnClickListener(new View.OnClickListener(){
           public void onClick(View view){
               //Conversion to string
               final String currentPasswordStr = currentPasswordText.getText().toString();
               newPasswordStr = newPasswordText.getText().toString();
               reEnterStr = reEnterText.getText().toString();
               validNewPassword = false;
               validReEnter=false;
               validCurrent=false;

               //Check new password
               if(TextUtils.isEmpty(currentPasswordStr)){
                   currentPasswordText.setError("Please enter your current password");
               }else validCurrent = true;

               if (TextUtils.isEmpty(newPasswordStr)) {
                   newPasswordText.setError("Please enter your new password");
               } else if (!(isValidPassword(newPasswordStr))){
                   newPasswordText.setError("Min 8 characters with at least 1 Upper and 1 number");
               } else validNewPassword = true;

               if (TextUtils.isEmpty(reEnterStr)) {
                   reEnterText.setError("Please enter your new password");
               } else if (!(TextUtils.equals(newPasswordStr,reEnterStr))) {
                   reEnterText.setError("Password does not match");
                   newPasswordText.setError("Password does not match");
               } else validReEnter = true;


               if(validNewPassword&&validReEnter&&validCurrent){
                   System.out.println("Passwords valid");
                   AuthCredential credential = EmailAuthProvider
                           .getCredential(email, currentPasswordStr);
                   user.reauthenticate(credential)
                           .addOnCompleteListener(new OnCompleteListener<Void>() {
                               @Override
                               public void onComplete(@NonNull Task<Void> task) {
                                   if(task.isSuccessful()){
                                       System.out.println("User re-authenticated.");
                                       user.updatePassword(newPasswordStr)
                                               .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                   @Override
                                                   public void onComplete(@NonNull Task<Void> task) {
                                                       if (task.isSuccessful()) {
                                                           Toast.makeText(getActivity(),"Password updated",Toast.LENGTH_SHORT).show();
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
                           }).addOnFailureListener(new OnFailureListener() {
                               @Override
                               public void onFailure(@NonNull Exception e) {
                                   Toast.makeText(getActivity(),"User not authenticated. Try again.",Toast.LENGTH_SHORT).show();
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
    /*private void showDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Re-key in your credentials");

        // Set up the input
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
                                    System.out.println("User re-authenticated.");
                                    user.updatePassword(newPasswordStr)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(getActivity(),"Password updated",Toast.LENGTH_SHORT).show();
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
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                //Put function after clicking cancel
                dialog.cancel();
            }
        });

        builder.show();
    }*/
}