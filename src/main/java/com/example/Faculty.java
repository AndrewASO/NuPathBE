package com.example;


/**
 * Manages faculty selections for users, enabling updates to MongoDB. It integrates with the system's
 * task management and leaderboard update mechanisms.
 * @Date: 4-3-2023
 */



import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
 
import java.util.ArrayList;

/**
 * The Faculty class represents the functionality for users to select faculty members
 * and updates these selections to a MongoDB database. It's part of the task management system,
 * influencing how leaderboard updates are made based on user selections.
 */
public class Faculty implements Task {

    private ArrayList<String> facultyNames; // Stores the names of selected faculty members
    private User user; // The user making the selection
    private GetDbCollection mongoDB = new GetDbCollection(); // Utility for accessing MongoDB collections
    private MongoClient mongoClient; // MongoDB client for database operations

    /**
     * Initializes a new instance of the Faculty class for a given user.
     * 
     * @param user The user associated with faculty selections.
     */
    public Faculty(User user) {
        this.user = user;
        // Note: The original constructor does not assign mongoClient passed as a parameter.
        // If mongoClient is intended to be passed, the constructor signature and assignment should be updated accordingly.
    }

    /**
     * Records a faculty member selected by the user.
     * 
     * @param faculty The name of the faculty member selected.
     */
    public void addFaculty(String faculty) {
        // Potentially intended to add to facultyNames list; currently, it updates the user's faculty selection.
        this.user.setFacultySelection(faculty);
    }

    /**
     * Completes the faculty selection task by inserting the user's choice into a MongoDB collection.
     * This action supports the integration with the system's leaderboard, which can be updated based
     * on the stored faculty selections.
     */
    public void completeTask() {
        // Note: Variable name corrected from dormCollection to facultyCollection for clarity.
        MongoCollection<Document> facultyCollection = mongoDB.returnCollection("Tasks", "FacultySelection", mongoClient);
        Document document = new Document("Username", user.getUsername()).append("Display Name", user.getDisplayName());
        facultyCollection.insertOne(document);
    }
}
