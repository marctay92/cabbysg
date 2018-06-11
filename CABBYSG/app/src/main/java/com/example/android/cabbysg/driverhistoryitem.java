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

    public driverhistoryitem(String tripID, String riderID, String firstName, String lastName, String pickUpLocation, String destination, String tripDate, String tripTime, String selectedRoute, String fare, String rating){
        this.tripID =tripID;
        this.riderID =riderID;
        this.firstName=firstName;
        this.lastName=lastName;
        this.pickUpLocation = pickUpLocation;
        this.destination=destination;
        this.tripDate=tripDate;
        this.tripTime=tripTime;
        this.selectedRoute=selectedRoute;
        this.fare=fare;
        this.rating=rating;
    }

    public String getRiderID() {
        return riderID;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
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
    public String getRating(){return rating;}
}
