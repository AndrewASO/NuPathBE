package com.example;

/**
 * This allows the user to select a faculty member and then update it to the mongodb, and then whenever the 
 * leaderboard decides to update, then it'll be updated with the information saved to MongoDB.
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

import java.util.ArrayList;




/**
 * 
 */
public class Faculty implements Task{

    private ArrayList<String> facultyNames;
    private User user;
    private GetDbCollection mongoDB = new GetDbCollection();
    private MongoClient mongoClient;


    public Faculty(User user){
        this.user = user;
        this.mongoClient = mongoClient;
    }

    public void addFaculty(String faculty){
        this.user.setFacultySelection(faculty);
    }



    public void completeTask(){

        MongoCollection<Document> dormCollection = mongoDB.returnCollection("Tasks", "FacultySelection", mongoClient);
        Document document = new Document("Username", user.getUsername() ).append("Display Name", user.getDisplayName() );
        dormCollection.insertOne(document);
    }

}