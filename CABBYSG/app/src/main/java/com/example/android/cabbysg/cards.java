package com.example.android.cabbysg;


public class cards {
    private String location;
    private String destination;
    private String fare;
    private String selectedRoute;
    private String fareType;
    private String dateTime;

    public cards (String location, String destination, String fare, String selectedRoute, String fareType, String dateTime){
        this.location = location;
        this.destination = destination;
        this.fare = fare;
        this.selectedRoute = selectedRoute;
        this.fareType = fareType;
        this.dateTime = dateTime;
    }
    public String getLocation(){
        return location;
    }
    public void setLocation(String location){
        this.location = location;
    }
    public String getDestination(){
        return destination;
    }
    public void setDestination(String destination){
        this.destination = destination;
    }
    public String getFare(){
        return fare;
    }
    public void setFare(String fare){
        this.fare= fare;
    }
    public String getSelectedRoute(){
        return selectedRoute;
    }
    public void setSelectedRoute(String selectedRoute){
        this.selectedRoute= selectedRoute;
    }

    public String getFareType(){
        return fareType;
    }
    public void setFareType(String fareType){
        this.fareType= fareType;
    }

    public String getDateTime() {
        return dateTime;
    }
    public void setDateTime(String dateTime){
        this.dateTime = dateTime;
    }
}

