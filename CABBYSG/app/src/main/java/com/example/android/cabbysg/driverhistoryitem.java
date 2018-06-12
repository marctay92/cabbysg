package com.example.android.cabbysg;

public class driverhistoryitem {
    public String tripID;
    public String riderID;
    public String firstName;
    public String lastName;

    public String pickUpLocation;
    public String destination;
    public String tripDate;
    public String tripTime;
    public String selectedRoute;
    public String rating;
    public String fare;

    public driverhistoryitem(String tripID, String riderID,String pickUpLocation, String destination, String tripDate, String tripTime, String selectedRoute, String fare){
        this.tripID =tripID;
        this.riderID =riderID;
        this.pickUpLocation = pickUpLocation;
        this.destination=destination;
        this.tripDate=tripDate;
        this.tripTime=tripTime;
        this.selectedRoute=selectedRoute;
        this.fare=fare;
    }
}
