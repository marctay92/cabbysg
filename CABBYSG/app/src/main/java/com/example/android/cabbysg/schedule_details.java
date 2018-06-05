package com.example.android.cabbysg;

public class schedule_details {

    public String schedule_date;
    public String schedule_time;
    public String schedule_name;
    public String schedule_carplatenumber;
    public String schedule_rating;
    public String schedule_destination;
    public String schedule_location;
    public String schedule_type;
    public String schedule_price;

    public schedule_details(String schedule_date, String schedule_time, String schedule_name,
                            String schedule_carplatenumber, String schedule_rating,
                            String schedule_destination, String schedule_location,
                            String schedule_type, String schedule_price) {
        this.schedule_date = schedule_date;
        this.schedule_time = schedule_time;
        this.schedule_name = schedule_name;
        this.schedule_carplatenumber = schedule_carplatenumber;
        this.schedule_rating = schedule_rating;
        this.schedule_destination = schedule_destination;
        this.schedule_location = schedule_location;
        this.schedule_type = schedule_type;
        this.schedule_price = schedule_price;
    }


}
