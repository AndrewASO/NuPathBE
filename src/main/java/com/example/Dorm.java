package com.example;

/**
 * Implements task-related functionalities specifically for managing dormitory selections by users.
 * It facilitates the interaction with the MongoDB database to persist user dormitory choices and supports
 * the integration with a task completion mechanism.
 * 
 * @Date: 4-3-2023
 */

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

 

/**
 * The Dorm class is responsible for handling dormitory selections made by users.
 * It implements the Task interface to integrate with the system's task management, allowing
 * for the updating of dormitory selection data within the MongoDB database.
 */
public class Dorm implements Task {

    private User user; // The user making the dorm selection
    private GetDbCollection mongoDB = new GetDbCollection(); // Utility for accessing MongoDB collections
    private MongoClient mongoClient; // MongoDB client for database operations

    /**
     * Constructs a Dorm instance associated with a specific user and MongoDB client.
     * 
     * @param user The user associated with the dorm selection.
     * @param mongoClient The MongoClient used for database interactions.
     */
    public Dorm(User user, MongoClient mongoClient) {
        this.user = user;
        this.mongoClient = mongoClient;
    }

    /**
     * Updates the user's dormitory selection.
     * 
     * @param dorm The dormitory selected by the user.
     */
    public void addDorm(String dorm) {
        this.user.setDormSelection(dorm);
    }

    /**
     * Completes the dormitory selection task by inserting the selection data into the MongoDB collection.
     * This facilitates the update of user dormitory choices and potentially influences related systems,
     * such as leaderboard updates or dormitory assignments.
     */
    public void completeTask() {
        MongoCollection<Document> dormCollection = mongoDB.returnCollection("Tasks", "DormSelection", mongoClient);
        Document document = new Document("Username", user.getUsername())
                .append("Display Name", user.getDisplayName());
        dormCollection.insertOne(document);
    }
}
