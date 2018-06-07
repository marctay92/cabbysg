package com.example.android.cabbysg;

public class historyDetails {
    public String tripID;
    public String driverID;
    public String firstName;
    public String lastName;
    public String model;
    public String regNum;
    public String pickUpLocation;
    public String destination;
    public String tripDate;
    public String tripTime;
    public String selectedRoute;
    public String fare;

    public historyDetails(String tripID, String driverID, String firstName, String lastName, String model, String regNum, String pickUpLocation, String destination, String tripDate, String tripTime, String selectedRoute, String fare){
        this.tripID =tripID;
        this.driverID =driverID;
        this.firstName=firstName;
        this.lastName=lastName;
        this.model=model;
        this.regNum=regNum;
        this.pickUpLocation = pickUpLocation;
        this.destination=destination;
        this.tripDate=tripDate;
        this.tripTime=tripTime;
        this.selectedRoute=selectedRoute;
        this.fare=fare;
    }

    public String getDriverID() {
        return driverID;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getModel() {
        return model;
    }

    public String getRegNum() {
        return regNum;
    }

    public String getPickUpLocation() {
        return pickUpLocation;
    }

    public String getDestination() {
        return destination;
    }

    public String getTripDate() {
        return tripDate;
    }

    public String getTripTime() {
        return tripTime;
    }

    public String getSelectedRoute() {
        return selectedRoute;
    }

    public String getFare() {
        return fare;
    }
}
