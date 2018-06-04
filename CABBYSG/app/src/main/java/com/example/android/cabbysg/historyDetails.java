package com.example.android.cabbysg;

import android.widget.ImageView;

public class historyDetails {
    public String historyDriver;
    public String historyVeh;
    public String historyDate;
    public String historyTime;
    public String historyPrice;

    public historyDetails(String historyDriver, String historyVeh, String historyDate, String historyTime, String historyPrice){
        this.historyDriver =historyDriver;
        this.historyVeh=historyVeh;
        this.historyDate=historyDate;
        this.historyTime=historyTime;
        this.historyPrice=historyPrice;
    }
}
