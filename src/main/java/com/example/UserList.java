package com.example;

/*
 * This is for webserver, so everything can be ran through here except for a couple of the classes that 
 * can be ran on their own like leaderboard. It'll update all of the tasks that need updating, along with 
 * storing all of the users within an array to grab their information for the webserver.
 * @Date: 4-3-2023
 */



import java.util.ArrayList;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;


public class UserList {
    
    private MongoClient mongoClient = null;
    //private static UserList instance = new UserList( MongoClient );
    private ArrayList<User> userArray = new ArrayList<>();
    private GetDbCollection mongoDB = new GetDbCollection();
    private HAM ham;
    private Facilities facilities;
    private Faculty faculty;
    private Classes classes;
    private Dorm dorm;
    private Forums forum = new Forums( this.mongoClient );

    public UserList( MongoClient mongoClient ){

        this.mongoClient = mongoClient;
        MongoCollection<Document> userCollection = mongoDB.returnCollection("UserDatabase", "Users", mongoClient);
        for(Document doc : userCollection.find() ){
            String username = doc.getString("Username");
            String pw = doc.getString("Password");
            userArray.add( new User(username, pw) );
        }
    }

    public MongoClient returnMongoClient(){
        return this.mongoClient;
    }

    public String returnAllUserNames(){

        String allUserNames = "";

        MongoCollection<Document> userCollection = mongoDB.returnCollection("UserDatabase", "Users", mongoClient);

        for(Document doc : userCollection.find() ){
            allUserNames += doc.getString("Username") + ", ";
        }

        return allUserNames;

    }


    public String checkUsername(String userName){

        MongoCollection<Document> userCollection = mongoDB.returnCollection("UserDatabase", "Users", mongoClient);

        for(Document doc : userCollection.find() ){
            if( userName.compareTo( doc.getString("Username") ) == 0 ){
                User user = new User( userName, doc.getString("Password") );
                addUser(user);
                return "True";
            }
        }


        return "False";

    }


    public String checkLogin(String username, String pw){

        MongoCollection<Document> userCollection = mongoDB.returnCollection("UserDatabase", "Users", mongoClient);
        
        String newUsername = null;
        String password = null;

        for(Document doc : userCollection.find() ){

            if( (username.compareTo( doc.getString("Username") ) == 0) && (pw.compareTo( doc.getString("Password") ) == 0) ){
                newUsername = doc.getString("Username");
                password = doc.getString("Password");
                break;
            }
        }

        if(newUsername != null && password != null){
            User user = accessUser(newUsername);
            //addUser(oldUser);
            return "True";
        }
        else{
            return "False";
        }
    }


    public boolean createUser(String displayName, String username, String password, String contactInfo){
        
        MongoCollection<Document> userCollection = mongoDB.returnCollection("UserDatabase", "Users", mongoClient);

        boolean ifUser = false;
        for(Document doc : userCollection.find() ){

            if( username.compareTo( doc.getString("Username") ) == 0){
                ifUser = true;
            }
        }

        if(ifUser){
            return false;
        }
        else{
            User newUser = new User(displayName, username, password, contactInfo, mongoClient);
            addUser( newUser );
            return true;
        }   

    }


    private void addUser(User user){
        this.userArray.add(user);
    }

    public void removeUser(User user){
        //this.userArray.remove(user);
    }

    public User accessUser(String userName){

        for(User user : this.userArray){
            if( userName.compareTo( user.getUsername() ) == 0){
                return user; //Breaks out of for loop
            }
        }

        //User should be found, so this line should never run
        return null;
    }

    /**
     * This is for forums if you want to see all of the online users.
     * @return all of the user in the User ArrayList
     */
    public ArrayList<User> getUsersArray(){
        return userArray;
    }

    /** 
    public static UserList getInstance(){
        return instance;
    }*/

    public Forums getForum(){
        return this.forum;
    }
    
}
