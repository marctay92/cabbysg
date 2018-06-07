package com.example.android.cabbysg;

public class DriverInfo {
    public String firstName;
    public String lastName;
    public String mobileNum;
    public String email;
    public String regNum;
    public String model;

    public DriverInfo(){

    }

    public DriverInfo(String firstName, String lastName,String mobileNum, String email, String regNum, String model){
        this.firstName = firstName;
        this.lastName = lastName;
        this.mobileNum = mobileNum;
        this.email = email;
        this.regNum = regNum;
        this.model = model;
    }
}
