package com.example.android.cabbysg;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class editPassword extends Fragment {

    EditText currentPasswordText;
    EditText newPasswordText;
    EditText reEnterText;
    Button submitBtn;

    public editPassword() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View rootView = inflater.inflate(R.layout.fragment_edit_password, container, false);

       currentPasswordText=(EditText)rootView.findViewById(R.id.currentPassword);
       newPasswordText=(EditText)rootView.findViewById(R.id.newPassword);
       reEnterText=(EditText)rootView.findViewById(R.id.reEnter);
       submitBtn=(Button)rootView.findViewById(R.id.confirmChange);

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

//>              /if(validNewPassword&&validReEnter&&validCurrent){Save to database}
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