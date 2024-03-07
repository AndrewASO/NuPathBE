package com.example;

import com.mongodb.MongoClient;

/**
 * This is the leaderboard where the information can be updated with the 5 tasks within the Task Database, then updated
 * to the leaderboard collection where it'll be returned in point order.
 * @Date: 4-3-2023
 */



import com.mongodb.client.MongoCollection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.bson.Document;




public class LeaderBoard {
    
    private MongoClient mongoClient;
    private GetDbCollection mongoDB = new GetDbCollection();

    public LeaderBoard( MongoClient mongoClient ){
        this.mongoClient = mongoClient;
    }


    public void getTasksInformation(){
        
        MongoCollection<Document> dormCollection = mongoDB.returnCollection("Tasks", "DormSelection", mongoClient);
        MongoCollection<Document> classCollection = mongoDB.returnCollection("Tasks", "ClassSelection", mongoClient);
        MongoCollection<Document> facilitiesCollection = mongoDB.returnCollection("Tasks", "FacilitiesSelection", mongoClient);
        MongoCollection<Document> facultyCollection = mongoDB.returnCollection("Tasks", "FacultySelection", mongoClient);
        MongoCollection<Document> foodCollection = mongoDB.returnCollection("Tasks", "FoodSelection", mongoClient);

        
        for(Document doc : dormCollection.find() ){
            String username = doc.getString("Username");
            updateLeaderBoard(username, "Dorm");
        }

        for(Document doc : classCollection.find() ){
            String username = doc.getString("Username");
            updateLeaderBoard(username, "Class");
        }

        for(Document doc : facilitiesCollection.find() ){
            String username = doc.getString("Username");
            updateLeaderBoard(username, "Facilities");
        }   

        for(Document doc : facultyCollection.find() ){
            String username = doc.getString("Username");
            updateLeaderBoard(username, "Faculty");
        }

        for(Document doc : foodCollection.find() ){
            String username = doc.getString("Username");
            updateLeaderBoard(username, "Food");
        }


    }


    public void updateLeaderBoard(String username, String field){

        MongoCollection<Document> lbCollection = mongoDB.returnCollection("Tasks", "Leaderboard", mongoClient);

        for(Document doc : lbCollection.find() ){
            if(username.compareTo(doc.getString("Username") ) == 0 && doc.getString(field).compareTo("False") == 0){
                mongoDB.updateDatabase("Tasks", "Leaderboard", username, field, "True", mongoClient);
                int points = Integer.valueOf( doc.getString("Points") );
                points += 100;
                mongoDB.updateDatabase("Tasks", "Leaderboard", username, "Points", String.valueOf(points), mongoClient);
                //Need to remember to delete the database
            }
            //Do nothing if the if conditions aren't met.
        }

    }

    public ArrayList<ArrayList<String>> returnLists(){
        ArrayList<ArrayList<String>> lbInformation = new ArrayList<>();

        MongoCollection<Document> lbCollection = mongoDB.returnCollection("Tasks", "Leaderboard", mongoClient);

        for(Document doc : lbCollection.find() ){
            ArrayList<String> userPts = new ArrayList<>();
            String username = doc.getString("Username");
            userPts.add(username);
            String strPts = doc.getString("Points");
            userPts.add(strPts);
            lbInformation.add( userPts );
        }

        //Sorts  the 2d array objects
        Collections.sort( lbInformation, new Comparator< ArrayList<String> >() {

            @Override
            public int compare(ArrayList<String> o1, ArrayList<String> o2) {
                return o1.get(1).compareTo( o2.get(1) );
            }
            
        });

        //Collections.sort( lbInformation, Collections.reverseOrder() );

        

        return lbInformation;
    }

}
