package com.example;

/**
 * Represents an entity for handling class information, including its storage and retrieval from a MongoDB database.
 * It aims to manage class details such as name, professors, timings, and associated workshops.
 * @Date: 4-3-2023
 */

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import io.github.cdimascio.dotenv.Dotenv;
import java.util.ArrayList;


public class Class {

    private String name, professors, startTime, endTime, startDate, endDate;
    private ArrayList<Days> days;
    private Workshop workshop;

    /**
     * Constructs a Class instance with detailed information.
     */
    public Class(String name, String professors, String startTime, String endTime, String startDate, String endDate, ArrayList<Days> days) {
        this.name = name;
        this.professors = professors;
        this.startTime = startTime;
        this.endTime = endTime;
        this.startDate = startDate;
        this.endDate = endDate;
        this.days = days;
    }

    // Overloaded constructor - purpose and parameters need clarification or adjustment.
    public Class(String string, String string2, String string3, String string4, String string5, String string6, Days tuesday, Days thursday) {
        // Implementation needed
    }

    // Getters
    public String getName() { return name; }
    public String getProfessors() { return professors; }
    public String getStartTime() { return startTime; }
    public String getEndTime() { return endTime; }
    public String getStartDate() { return startDate; }
    public String getEndDate() { return endDate; }
    public ArrayList<Days> getDays() { return days; }
    public Workshop getWorkshop() { return workshop; }

    /**
     * Converts the days of the week when the class meets into a single string.
     */
    public String convertDaysToString() {
        String daysString = "";
        for (Days day : days) {
            daysString += day + " ";
        }
        return daysString.trim();
    }

    /**
     * Adds a workshop to the class.
     */
    public void addWorkshop(String startTime, String endTime, Days days) {
        this.workshop = new Workshop(startTime, endTime, this.startDate, this.endDate, days);
    }

    /**
     * Saves class information to a MongoDB database.
     */
    public void saveClass() {
        Dotenv dotenv = Dotenv.load();
        String mongodbUrl = dotenv.get("MONGODB_URL");

        try (MongoClient mongoClient = new MongoClient(new MongoClientURI(mongodbUrl))) {
            MongoDatabase database = mongoClient.getDatabase("ClassDB");
            MongoCollection<Document> collection = database.getCollection("Classes");

            Document document = new Document("Class Title", name)
                    .append("Professor(s)", professors)
                    .append("Start Time", startTime)
                    .append("End Time", endTime)
                    .append("Start Date", startDate)
                    .append("End Date", endDate)
                    .append("Days", convertDaysToString());
            collection.insertOne(document);
        }
    }

    /**
     * Returns a string representation of the class for debugging and logging purposes.
     */
    @Override
    public String toString() {
        return "Class - Name: " + name + ", Professor(s): " + professors + ", Start Time: " + startTime + ", End Time: " + endTime
                + ", Start Date: " + startDate + ", End Date: " + endDate + ", Days: " + convertDaysToString();
    }
}
