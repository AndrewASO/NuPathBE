package com.example;

/**
 * Represents a Workshop with defined start and end times, as well as dates, and the day(s) it occurs on.
 * This class provides a structured way to handle workshop scheduling within the application,
 * allowing for easy management and retrieval of workshop details.
 * 
 * @Date: 4-3-2023
 */
public class Workshop {

    String startTime; // The start time of the workshop
    String endTime;   // The end time of the workshop
    String startDate; // The start date of the workshop
    String endDate;   // The end date of the workshop
    Days days;        // The day(s) the workshop takes place, assuming workshops generally occur on a single day

    /**
     * Constructs a Workshop instance with specified time, date, and day(s) of occurrence.
     * 
     * @param startTime The start time of the workshop.
     * @param endTime The end time of the workshop.
     * @param startDate The start date of the workshop.
     * @param endDate The end date of the workshop.
     * @param days The day(s) on which the workshop occurs.
     */
    public Workshop(String startTime, String endTime, String startDate, String endDate, Days days){
        this.startTime = startTime;
        this.endTime = endTime;
        this.startDate = startDate;
        this.endDate = endDate;
        this.days = days;
    }

    /**
     * Provides a string representation of the Workshop instance, detailing its start and end times.
     * 
     * @return A string summarizing the workshop schedule.
     */
    public String toString(){
        return "Workshop - StartTime: " + this.startTime + ", EndTime: " + this.endTime;
    }
}
