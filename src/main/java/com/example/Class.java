package com.example;

/**
 * @Date: 4-3-2023
 */



import com.mongodb.BasicDBObject;
import com.mongodb.ConnectionString;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoCredential;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.types.ObjectId;


//Maybe just create the class and look for days that matches Days, but time doesn't need to be displayed besides start/endtime
//Not like google calendar
public class Class {
    
    private String name, professors, startTime, endTime, startDate, endDate;
    private ArrayList<Days> days;
    private Workshop workshop;

    public Class(String name, String professors, String startTime, String endTime, String startDate, String endDate, ArrayList<Days> days){
        this.name = name;
        this.professors = professors;
        this.startTime = startTime;
        this.endTime = endTime;
        this.startDate = startDate;
        this.endDate = endDate;
        this.days = days;
    }

    public Class(String string, String string2, String string3, String string4, String string5, String string6,
            Days tuesday, Days thursday) {
    }

    public String getName(){
        return name;
    }

    public String getProfessors(){
        return professors;
    }

    public String getStartTime(){
        return startTime;
    }

    public String getEndTime(){
        return endTime;
    }

    public String getStartDate(){
        return startDate;
    }

    public String getEndDate(){
        return endDate;
    }

    public ArrayList<Days> getDays(){
        return days;
    }

    public Workshop getWorkshop(){
        return workshop;
    }


    public String convertDaysToString(){

        String daysString = "";

        for(int i = 0; i < days.size(); i++){
            daysString += days.get(i) + " " ;
        }

        return daysString;
    }

    public void addWorkshop(String startTime, String endTime, Days days){
        this.workshop = new Workshop(startTime, endTime, this.startDate, this.endDate, days);
    }


    public void saveClass(){


        //Opening database
        MongoClientURI uri = new MongoClientURI("mongodb+srv://nuPathLogin:08426%21%23%25Nnn@nupath.gkq49uo.mongodb.net/test");
        MongoClient mongoClient = new MongoClient(uri);

        //Accessing ClassDB database, then getting the Classes collection
        MongoDatabase database = mongoClient.getDatabase("ClassDB");
        MongoCollection<Document> collection = database.getCollection("Classes");

        //Creating new document to insert into the FacultySelection collection, so leaderboard can grab the information later
        Document document = new Document("Class Title", name).append("Professor(s)", professors).append("Start Time", startTime)
        .append("End Time", endTime).append("Start Date", startDate).append("End Date", endDate).append("Days", convertDaysToString() );
        collection.insertOne(document);

        //Close the mongoClient and prevent this from keeping the connection up to the database
        mongoClient.close();
    }

    //Not rly needed but useful when bug testing and prob should be commented out when nearing final product or done bug testing
    public String toString(){


        return "Class - Name: " + name + ", Professor(s): " + professors + ", startTime: " + startTime + ",endTime: " + endTime
        + ", startDate: " + startDate + ", endDate: " + endDate + ", Days: " + convertDaysToString();
    }
    

}
