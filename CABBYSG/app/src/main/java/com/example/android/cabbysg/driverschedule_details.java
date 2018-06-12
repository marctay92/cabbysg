package com.example.android.cabbysg;

public class driverschedule_details {

    public String driverschedule_id;
    public String driverschedule_riderID;
    public String driverschedule_date;
    public String driverschedule_destination;
    public String driverschedule_location;
    public String driverschedule_type;
    public String driverschedule_price;

    public driverschedule_details(String driverschedule_id,String driverschedule_date, String driverschedule_riderID,
                                  String driverschedule_destination, String driverschedule_location,
                                  String driverschedule_type, String driverschedule_price) {
        this.driverschedule_id = driverschedule_id;
        this.driverschedule_date = driverschedule_date;
        this.driverschedule_riderID = driverschedule_riderID;
        this.driverschedule_destination = driverschedule_destination;
        this.driverschedule_location = driverschedule_location;
        this.driverschedule_type = driverschedule_type;
        this.driverschedule_price = driverschedule_price;
    }
}