package com.example;

/**
 * Handles the user's facility preferences and selections, integrating these choices into the application's task
 * management and updating the MongoDB database accordingly.
 *
 * @Date: 4-3-2023
 */


import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

/**
 * The Facilities class is responsible for managing facilities selections by users, reflecting their preferences
 * for various facilities. It implements the Task interface, allowing for seamless integration with the system's
 * task completion tracking and updates to the MongoDB database to store these preferences.
 */
public class Facilities implements Task {

    private User user; // The user making the facility selection
    private GetDbCollection mongoDB = new GetDbCollection(); // Utility for accessing MongoDB collections
    private MongoClient mongoClient; // MongoDB client for database operations

    /**
     * Constructs a Facilities instance for a given user and database client, initializing the class with
     * the necessary context for interacting with the database based on the user's actions.
     * 
     * @param user The user associated with this instance, whose facility preferences are being managed.
     * @param mongoClient The MongoClient used to interact with the database.
     */
    public Facilities(User user, MongoClient mongoClient) {
        this.user = user;
        this.mongoClient = mongoClient;
    }

    /**
     * Updates the user's facility preferences by invoking a method on the User object to update the user's
     * selection of facilities.
     * 
     * @param facilities The new facilities preferences selected by the user.
     */
    public void addFacilities(String facilities) {
        this.user.setFacilitiesSelection(facilities);
    }

    /**
     * Finalizes the task of selecting facilities by inserting the user's choice into a designated collection
     * within the MongoDB database. This method supports the broader task completion system by logging the
     * user's facility preferences.
     */
    public void completeTask() {
        MongoCollection<Document> facilitiesCollection = mongoDB.returnCollection("Tasks", "FacilitiesSelection", mongoClient);
        Document document = new Document("Username", user.getUsername())
                .append("Display Name", user.getDisplayName());
        facilitiesCollection.insertOne(document);
    }
}
