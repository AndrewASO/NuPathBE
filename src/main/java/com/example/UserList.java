package com.example;

/*
 * Centralizes user management for a webserver, enabling user authentication, registration, and data retrieval.
 * Integrates with MongoDB to persist user information, supporting various user-related operations and interactions.
 * Designed to work within a webserver environment, facilitating task updates and forum interactions.
 * @Date: 4-3-2023
 */

import java.util.ArrayList;
import org.bson.Document;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;

/**
 * Manages the collection of users, supporting operations such as authentication, registration, and information retrieval.
 * This class plays a critical role in the webserver's functionality, interacting with MongoDB to update and retrieve user data.
 */
public class UserList {
    
    private MongoClient mongoClient = null;
    private ArrayList<User> userArray = new ArrayList<>();
    private GetDbCollection mongoDB = new GetDbCollection();
    private HAM ham;
    private Facilities facilities;
    private Faculty faculty;
    private Classes classes;
    private Dorm dorm;
    private Forums forum = new Forums( this.mongoClient );


    /**
     * Constructs a UserList, initializing it with a MongoDB client and populating the user list from the database.
     * @param mongoClient The MongoClient for database interaction.
     */
    public UserList( MongoClient mongoClient ){

        this.mongoClient = mongoClient;
        MongoCollection<Document> userCollection = mongoDB.returnCollection("UserDatabase", "Users", mongoClient);
        for(Document doc : userCollection.find() ){
            String username = doc.getString("Username");
            String pw = doc.getString("Password");
            userArray.add( new User(username, pw) );
        }
    }


    /**
     * Returns the MongoClient used by this UserList.
     * @return The MongoClient instance.
     */
    public MongoClient returnMongoClient(){
        return this.mongoClient;
    }


    /**
     * Compiles and returns a string of all usernames in the user list.
     * @return Comma-separated string of usernames.
     */
    public String returnAllUserNames(){

        String allUserNames = "";

        MongoCollection<Document> userCollection = mongoDB.returnCollection("UserDatabase", "Users", mongoClient);

        for(Document doc : userCollection.find() ){
            allUserNames += doc.getString("Username") + ", ";
        }

        return allUserNames;

    }


    /**
     * Checks if a given username exists in the user list.
     * @param userName The username to check.
     * @return "True" if the username exists, otherwise "False".
     */
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

    /**
     * Validates the provided login credentials against the stored user information.
     * @param username The username to validate.
     * @param pw The password to validate.
     * @return "True" if the credentials match, otherwise "False".
     */
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


    /**
     * Attempts to create and register a new user with the provided details.
     * @param displayName The display name for the new user.
     * @param username The desired username.
     * @param password The chosen password.
     * @param contactInfo The user's contact information.
     * @return True if the user is successfully created, false if the username already exists.
     */
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

    /**
     * Adds a user to the internal list of users.
     * @param user The User object to add.
     */
    private void addUser(User user){
        this.userArray.add(user);
    }

    public void removeUser(User user){
        //this.userArray.remove(user);
    }

    /**
     * Retrieves a User object for a specified username.
     * @param userName The username of the User to retrieve.
     * @return The User object if found, otherwise null.
     */
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
     * Provides access to the current list of users.
     * @return An ArrayList of User objects.
     */
    public ArrayList<User> getUsersArray(){
        return userArray;
    }

    /**
     * Provides access to the forum functionality.
     * @return The Forums instance associated with this UserList.
     */
    public Forums getForum(){
        return this.forum;
    }
    
}
