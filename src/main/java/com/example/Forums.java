package com.example;

/**
 * Manages forum interactions, including message posting and retrieval within an application.
 * Utilizes MongoDB for persisting forum messages and their metadata.
 *
 * @Date: 8-3-2023
 */



import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
 
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date; 

/**
 * The Forums class is responsible for handling user interactions within forum threads,
 * such as posting new messages and retrieving existing messages from a MongoDB database.
 */
public class Forums {

    private ArrayList<User> userList; // List of users participating in the forums
    private GetDbCollection mongoDB = new GetDbCollection(); // Utility for accessing MongoDB collections
    private MongoClient mongoClient; // MongoDB client for database operations

    /**
     * Constructs a Forums instance with a specific MongoDB client.
     * 
     * @param mongoClient The MongoClient used for database interactions.
     */
    public Forums(MongoClient mongoClient) {
        this.mongoClient = mongoClient;
    }

    /**
     * Updates the list of users participating in the forums.
     * 
     * @param updatedList The updated list of users.
     */
    public void updateUserList(ArrayList<User> updatedList) {
        this.userList = new ArrayList<>(updatedList);
    }

    /**
     * Adds a new message to the forum with the specified message content and display name of the user.
     * The message is timestamped and stored in the MongoDB database.
     * 
     * @param message The message content to be added.
     * @param displayName The display name of the user posting the message.
     */
    public void addMessage(String message, String displayName) {
        MongoCollection<Document> collection = mongoDB.returnCollection("Forum", "Messages", mongoClient);

        long currentTimeMillis = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy HH:mm");
        Date resultdate = new Date(currentTimeMillis);

        Document document = new Document("Display Name", displayName)
                .append("Message", message)
                .append("Time", sdf.format(resultdate));
        collection.insertOne(document);
    }

    /**
     * Retrieves all forum messages, organizing them into a list where each entry represents
     * a single message and its associated metadata (timestamp and display name).
     * 
     * @return A list of lists, where each inner list contains message metadata and content.
     */
    public ArrayList<ArrayList<String>> getMessages() {
        ArrayList<ArrayList<String>> messages = new ArrayList<>();

        MongoCollection<Document> messageCollection = mongoDB.returnCollection("Forum", "Messages", mongoClient);

        for (Document doc : messageCollection.find()) {
            ArrayList<String> userMsg = new ArrayList<>();
            userMsg.add(doc.getString("Time"));
            userMsg.add(doc.getString("Display Name"));
            userMsg.add(doc.getString("Message"));
            messages.add(userMsg);
        }

        return messages;
    }
}
