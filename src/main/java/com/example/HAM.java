package com.example;

/**
 * The HAM (Health and Meals) class is responsible for managing the food selections of users within an application.
 * It tracks user food preferences and updates these preferences in a MongoDB database.
 * This class implements the Task interface, indicating its actions are part of a broader set of tasks within the system.
 *
 * @Date: 4-3-2023
 */


import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import org.bson.Document; 


public class HAM implements Task {

    private User user; // The user making the food selection
    private GetDbCollection mongoDB = new GetDbCollection(); // Utility class for accessing MongoDB collections
    private MongoClient mongoClient; // MongoDB client for database interactions

    /**
     * Constructs a HAM instance for a given user and MongoDB client.
     * 
     * @param user The user associated with this HAM instance.
     * @param mongoClient The MongoClient used for interacting with the MongoDB database.
     */
    public HAM(User user, MongoClient mongoClient) {
        this.user = user;
        this.mongoClient = mongoClient;
    }

    /**
     * Updates the food selection for the user. This method directly updates the user's food selection preference.
     * 
     * @param food The food item selected by the user.
     */
    public void addFood(String food) {
        this.user.setFoodSelection(food);
    }

    /**
     * Completes the task of setting a food selection by inserting the selection data into the MongoDB collection.
     * This facilitates the update of user food preferences in the database, potentially influencing related systems
     * such as dietary recommendations or meal planning.
     */
    public void completeTask() {
        MongoCollection<Document> foodCollection = mongoDB.returnCollection("Tasks", "FoodSelection", mongoClient);
        Document document = new Document("Username", user.getUsername())
                .append("Display Name", user.getDisplayName());
        foodCollection.insertOne(document);
    }
}
