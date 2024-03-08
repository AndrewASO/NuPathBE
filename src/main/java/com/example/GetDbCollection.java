package com.example;

/**
 * Facilitates access to MongoDB collections and provides functionality to update documents within these collections.
 * Specifically designed to open collections based on provided database and collection names, and perform updates on
 * documents based on user specifications.
 * 
 * @Date 15-3-23
 */


import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.bson.conversions.Bson;

public class GetDbCollection {

    /**
     * Constructs an instance of GetDbCollection. This constructor currently does not perform any operations but
     * is included for potential future enhancements and consistency.
     */
    public GetDbCollection() {
        // Potential setup or initialization code can be added here.
    }

    /**
     * Retrieves a MongoCollection<Document> based on the specified database and collection names using the provided
     * MongoClient.
     * 
     * @param db The name of the database.
     * @param collection The name of the collection within the database.
     * @param mongoClient The MongoClient used to access the database.
     * @return The MongoCollection<Document> specified by the database and collection names.
     */
    public MongoCollection<Document> returnCollection(String db, String collection, MongoClient mongoClient) {
        // Note: The original code incorrectly used Webserver.mongoClient instead of the provided mongoClient parameter.
        MongoDatabase database = mongoClient.getDatabase(db);
        return database.getCollection(collection);
    }

    /**
     * Updates a specific field for a document within the specified database and collection, based on the username
     * and the new value for the field. This method assumes documents contain a "Username" field for identification.
     * 
     * @param database The name of the database.
     * @param collectionName The name of the collection.
     * @param username The username identifying the document to update.
     * @param field The field within the document to update.
     * @param updatedVar The new value for the field.
     * @param mongoClient The MongoClient used to perform the database operation.
     */
    public void updateDatabase(String database, String collectionName, String username, String field, String updatedVar, MongoClient mongoClient) {
        MongoCollection<Document> collection = returnCollection(database, collectionName, mongoClient);

        // The method iterates over all documents. Consider using a more efficient query to directly find and update the document.
        for (Document doc : collection.find()) {
            if (username.equals(doc.getString("Username"))) {
                Bson updates = Updates.set(field, updatedVar);
                collection.updateOne(doc, updates);
                break;
            }
        }
    }
}
