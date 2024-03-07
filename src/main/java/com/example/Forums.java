package com.example;

/**
 * 
 * @Date: 8-3-2023
 */


import com.mongodb.BasicDBObject;
import com.mongodb.ConnectionString;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoCredential;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.Document;
import org.bson.types.ObjectId;

public class Forums {
    
    private ArrayList<User> userList;
    private GetDbCollection mongoDB = new GetDbCollection();
    private MongoClient mongoClient;
    
    public Forums( MongoClient mongoClient){
        this.mongoClient = mongoClient;
    }


    public void updateUserList(ArrayList<User> updatedList){
        this.userList = new ArrayList<>();
        for(int i = 0; i < updatedList.size(); i++){
            userList.add( updatedList.get(i) );
        }
    }

    public void addMessage(String message, String displayName){

        MongoCollection<Document> collection = mongoDB.returnCollection("Forum", "Messages", mongoClient);
        



        long yourmilliseconds = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy HH:mm");    
        Date resultdate = new Date(yourmilliseconds);

        Document document = new Document("Display Name", displayName ).append("Message", message).append("Time", sdf.format(resultdate) );
        collection.insertOne(document);

    }

    public ArrayList<ArrayList<String>> getMessages(){
        ArrayList<ArrayList<String>> messages = new ArrayList<>();

        MongoCollection<Document> messageCollection = mongoDB.returnCollection("Forum", "Messages", mongoClient);

        for(Document doc : messageCollection.find() ){
            ArrayList<String> userMsg = new ArrayList<>();
            String time = doc.getString("Time");
            userMsg.add(time);
            String displayName = doc.getString("Display Name");
            userMsg.add(displayName);
            String message = doc.getString("Message");
            userMsg.add(message);
            messages.add(userMsg);
        }


        return messages;
    }

}
