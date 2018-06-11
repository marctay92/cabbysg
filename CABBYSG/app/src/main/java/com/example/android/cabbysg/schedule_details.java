package com.example.android.cabbysg;

public class schedule_details {

    public String schedule_ID;
    public String schedule_driverID;
    public String schedule_pic;
    public String schedule_date;
    public String schedule_time;
    public String schedule_driverName;
    public String schedule_carplatenumber;
    public String schedule_rating;
    public String schedule_destination;
    public String schedule_location;
    public String schedule_routeType;
    public String schedule_fare;

    public schedule_details(String schedule_ID, String schedule_date, String schedule_time,
                            String schedule_driverID, String schedule_pic, String schedule_driverName,
                            String schedule_carplatenumber, String schedule_rating,
                            String schedule_destination, String schedule_location,
                            String schedule_routeType, String schedule_fare) {
        this.schedule_ID = schedule_ID;
        this.schedule_date = schedule_date;
        this.schedule_time = schedule_time;
        this.schedule_driverID = schedule_driverID;
        this.schedule_pic = schedule_pic;
        this.schedule_driverName = schedule_driverName;
        this.schedule_carplatenumber = schedule_carplatenumber;
        this.schedule_rating = schedule_rating;
        this.schedule_destination = schedule_destination;
        this.schedule_location = schedule_location;
        this.schedule_routeType = schedule_routeType;
        this.schedule_fare = schedule_fare;
    }


}
