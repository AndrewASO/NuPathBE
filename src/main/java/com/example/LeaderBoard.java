package com.example;

/**
 * Manages and updates the leaderboard by aggregating task completion information from various collections
 * within the Tasks database. Updates are reflected in the leaderboard collection, sorted by points.
 * 
 * @Date: 4-3-2023
 */



import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
 
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
 
 
public class LeaderBoard {
    
    private MongoClient mongoClient; // MongoDB client for database interactions
    private GetDbCollection mongoDB = new GetDbCollection(); // Utility for accessing MongoDB collections

    /**
     * Initializes a LeaderBoard instance with a MongoDB client.
     * 
     * @param mongoClient The MongoClient used for interacting with the database.
     */
    public LeaderBoard(MongoClient mongoClient) {
        this.mongoClient = mongoClient;
    }

    /**
     * Gathers task completion information from multiple collections within the Tasks database and updates
     * the leaderboard collection accordingly.
     */
    public void getTasksInformation() {
        // Collections for each task type
        MongoCollection<Document> dormCollection = mongoDB.returnCollection("Tasks", "DormSelection", mongoClient);
        MongoCollection<Document> classCollection = mongoDB.returnCollection("Tasks", "ClassSelection", mongoClient);
        MongoCollection<Document> facilitiesCollection = mongoDB.returnCollection("Tasks", "FacilitiesSelection", mongoClient);
        MongoCollection<Document> facultyCollection = mongoDB.returnCollection("Tasks", "FacultySelection", mongoClient);
        MongoCollection<Document> foodCollection = mongoDB.returnCollection("Tasks", "FoodSelection", mongoClient);

        // Update leaderboard for each task type
        updateLeaderboardForCollection(dormCollection, "Dorm");
        updateLeaderboardForCollection(classCollection, "Class");
        updateLeaderboardForCollection(facilitiesCollection, "Facilities");
        updateLeaderboardForCollection(facultyCollection, "Faculty");
        updateLeaderboardForCollection(foodCollection, "Food");
    }

    /**
     * Updates the leaderboard for tasks of a specific type based on task completions in the given collection.
     * 
     * @param collection The MongoDB collection containing task completions for a specific task type.
     * @param field The field name corresponding to the task type in the leaderboard collection.
     */
    private void updateLeaderboardForCollection(MongoCollection<Document> collection, String field) {
        for (Document doc : collection.find()) {
            String username = doc.getString("Username");
            updateLeaderBoard(username, field);
        }
    }

    /**
     * Updates the leaderboard entry for a specific user based on the completion of a task identified by the field parameter.
     * If the task was not previously completed by the user, updates the task as completed and increments the user's points.
     * 
     * @param username The username of the user whose leaderboard entry is to be updated.
     * @param field The task type to be updated as completed.
     */
    public void updateLeaderBoard(String username, String field) {
        MongoCollection<Document> lbCollection = mongoDB.returnCollection("Tasks", "Leaderboard", mongoClient);

        // Increment points and update task completion status
        for (Document doc : lbCollection.find()) {
            if (username.equals(doc.getString("Username")) && "False".equals(doc.getString(field))) {
                mongoDB.updateDatabase("Tasks", "Leaderboard", username, field, "True", mongoClient);
                int points = Integer.parseInt(doc.getString("Points")) + 100;
                mongoDB.updateDatabase("Tasks", "Leaderboard", username, "Points", String.valueOf(points), mongoClient);
                break; // Exit loop once the user is found and updated
            }
        }
    }

    /**
     * Retrieves the leaderboard information, returning a sorted list of users and their points.
     * The list is sorted in ascending order of points.
     * 
     * @return A sorted list of leaderboard entries, each entry containing the username and points.
     */
    public ArrayList<ArrayList<String>> returnLists() {
        ArrayList<ArrayList<String>> lbInformation = new ArrayList<>();

        MongoCollection<Document> lbCollection = mongoDB.returnCollection("Tasks", "Leaderboard", mongoClient);

        for (Document doc : lbCollection.find()) {
            ArrayList<String> userPts = new ArrayList<>();
            String username = doc.getString("Username");
            userPts.add(username);
            String strPts = doc.getString("Points");
            userPts.add(strPts);
            lbInformation.add(userPts);
        }

        // Sorts the 2d array objects based on points converted to integers for accurate numerical sorting
        Collections.sort(lbInformation, new Comparator<ArrayList<String>>() {
            @Override
            public int compare(ArrayList<String> o1, ArrayList<String> o2) {
                int points1 = Integer.parseInt(o1.get(1));
                int points2 = Integer.parseInt(o2.get(1));
                return Integer.compare(points1, points2);
            }
        });

        return lbInformation;
    }


}