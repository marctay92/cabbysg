package com.example.android.cabbysg;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class UserInfo {
    private String firstName;
    private String lastName;
    private String mobileNum;
    private String email;

    public UserInfo(){

    }

    public UserInfo(String firstName, String lastName,String mobileNum, String email){
        this.firstName = firstName;
        this.lastName = lastName;
        this.mobileNum = mobileNum;
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getMobileNum() {
        return mobileNum;
    }


    public String getEmail() {
        return email;
    }
}
