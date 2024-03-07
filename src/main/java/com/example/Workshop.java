package com.example;

/**
 * @Date: 4-3-2023
 */


public class Workshop {

    String startTime;
    String endTime;
    String startDate;
    String endDate;
    Days days;  //Workshops are generally one day, so should be good to leave this as a Days variable instead of Days[] ? Dunno discuss in future

    public Workshop(String startTime, String endTime, String startDate, String endDate, Days days){
        this.startTime = startTime;
        this.endTime = endTime;
        this.startDate = startDate;
        this.endDate = endDate;
        this.days = days;
    }
    
    public String toString(){
        return "Workshop - StartTime: " + this.startTime + ",EndTime: " + this.endTime;
    }

}
