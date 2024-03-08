package com.example;


/**
 * This source file is part of the backend logic for updating class selection data and interfacing with a MongoDB database.
 * It includes the Classes class that implements the Task interface, focusing on class selection updates and leaderboard integration.
 * 
 * @Date: 4-3-2023
 */


import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

/**
 * The Classes class is responsible for managing class selections made by users and updating the corresponding
 * MongoDB collection to reflect these choices. It implements the Task interface, enabling integration with
 * the application's task completion and leaderboard update mechanisms.
 */
public class Classes implements Task {
    
    private String classSelected;
    private User user;
    private GetDbCollection mongoDB = new GetDbCollection();
    private MongoClient mongoClient;

    /**
     * Constructs a Classes instance associated with a specific user and MongoDB client.
     * 
     * @param user The user associated with the class selection.
     * @param mongoClient The MongoClient used for database interactions.
     */
    public Classes(User user, MongoClient mongoClient) {
        this.user = user;
        this.mongoClient = mongoClient;
    }
    
    /**
     * Records the class selected by the user.
     * 
     * @param classPicked The class selected by the user.
     */
    public void addClasses(String classPicked) {
        this.classSelected = classPicked;
    }

    /**
     * Completes the task of class selection by inserting the selection data into the MongoDB collection.
     * This method facilitates the updating of the leaderboard based on the class selections made by users.
     */
    public void completeTask() {
        MongoCollection<Document> classCollection = mongoDB.returnCollection("Tasks", "ClassSelection", mongoClient);
        Document document = new Document("Username", user.getUsername()).append("Display Name", user.getDisplayName());
        classCollection.insertOne(document);
    }
}
