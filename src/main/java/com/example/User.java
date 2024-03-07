package com.example;

/**
 * This is the user profile for whenever a user logins into their acct and its stored here.
 * It stores information like displayname, username, password, about me, interests, photo gallery, etc.
 * The information can be updated and retrieved.
 * @Date: 4-3-2023
 */


import java.util.ArrayList;
import java.util.Base64;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Updates;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.bson.Document;
import org.bson.conversions.Bson;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.awt.*;


/**
 * This is the class user where all of the return and get functions are stored inside of it 
 */
public class User {
    
    private String displayName, username, password, interests, foodSelections, facultySelections, facilitiesSlection, dormSelection, 
    aboutMe, pfpString, permissionLevel, contactInformation, catalystNotes, classes;    //All information stored like this
    private BufferedImage pfp;  //Can displayPFP for testing
    private ArrayList<String> photoGallery = new ArrayList<>(); //Stores Photos
    private GetDbCollection mongoDB = new GetDbCollection();    //This is to access mongoDB collections 
    private MongoClient mongoClient = null;


    /**
     * This is the constructor for when a user is first making their account
     * @param displayName
     * @param username
     * @param password
     */
    public User(String displayName, String username, String password, String contactInformation, MongoClient mongoClient){

        this.mongoClient = mongoClient;
        this.displayName = displayName;
        this.username = username;
        this.password = password;
        this.contactInformation = contactInformation;
        this.permissionLevel = "1";

        saveInformation();  //Only saves information when User is initialized
        MongoCollection<Document> leaderboard = mongoDB.returnCollection("Tasks", "Leaderboard", mongoClient);
        Document document = new Document("Points", "0").append("Display Name", this.displayName).append("Username", this.username)
        .append("Dorm", "False").append("Class", "False").append("Facilities", "False")
        .append("Faculty", "False").append("Food", "False");
        leaderboard.insertOne(document);
    }


   /**
    * Constructor for when user is logging in and User class is being updated with the information inside of MongoDB
    * This shouldn't be needed anymore with the recent change that has arisen
    * @param username
    * @param password
    */
    public User(String username, String password){
        accessUserInformation(username);
    }


    /**
     * Updates display name by going to MongoDB UserDatabase db, then to Users Collection
     * @param name
     */
    public void updateDisplayName(String name){
        this.displayName = name;
        mongoDB.updateDatabase("UserDatabase", "Users", this.username, "Display Name", this.displayName, mongoClient);
    }

    /**
     * Updates password by going to MongoDB UserDatabase db, then to Users Collection
     * @param password
     */
    public void updatePassword(String password){
        this.password = password;
        mongoDB.updateDatabase("UserDatabase", "Users", this.username, "Password", this.password, mongoClient);
    }

    /**
     * Updates PFP by going to MongoDB UserDatabase db, then to Users Collection
     * @param fileName
     */
    public void uploadPFP(String fileName){
        this.pfpString = fileName;
        mongoDB.updateDatabase("UserDatabase", "Users", this.username, "PFP", this.pfpString, mongoClient);
    }

    /**
     * Updates Contact Information by going to MongoDB UserDatabase db, then to Users Collection
     * @param contactInfo
     */
    public void updateContactInfo(String contactInfo){
        this.contactInformation = contactInfo;
        mongoDB.updateDatabase("UserDatabase", "Users", this.username, "Contact Information", this.contactInformation, mongoClient);
    }

    /**
     * Adds an image to Photo Gallery by going to MongoDB UserDatabase db, then to Users Collection
     * @param fileName
     */
    public void addImgToPhotos(String fileName){

        photoGallery.add( fileName );

        MongoCollection<Document> collection = mongoDB.returnCollection("UserDatabase", "Users", mongoClient);

        for(Document doc : collection.find() ){
            if( username.compareTo( doc.getString("Username") ) == 0 ){
                ArrayList<String> toUpdate = (ArrayList<String>) doc.get("Photo Gallery");
                Document query = new Document("Photo Gallery", toUpdate);
                Bson updates = Updates.combine(Updates.set("Photo Gallery", photoGallery) );
                collection.updateOne(query, updates);
                break;
            }
        }
    }

    /**
     * Updates interests by going to MongoDB UserDatabase db, then to Users Collection
     * @param interests
     */
    public void updateInterests(String interests){
        this.interests = interests;
        mongoDB.updateDatabase("UserDatabase", "Users", this.username, "Interests", this.interests, mongoClient);
    }

    /**
     * Updates catalyst notes by going to MongoDB UserDatabase db, then to Users Collection
     * @param catalystNotes
     */
    public void updateCatalystNote(String catalystNotes){
        this.catalystNotes = catalystNotes;
        mongoDB.updateDatabase("UserDatabase", "Users", this.username, "Catalyst Notes", this.catalystNotes, mongoClient);
    }

    /**
     * Updates about me by going to MongoDB UserDatabase db, then to Users Collection
     * @param aboutMe
     */
    public void updateAboutMe(String aboutMe){
        this.aboutMe = aboutMe;
        mongoDB.updateDatabase("UserDatabase", "Users", this.username, "About Me", this.aboutMe, mongoClient);
    }

    /**
     * Sets food selection for user and HAM that'll be sent to Tasks database.
     * @param foodSelection
     */
    public void setFoodSelection(String foodSelection){
        this.foodSelections = foodSelection;
        mongoDB.updateDatabase("UserDatabase", "Users", this.username, "Food Selection", this.foodSelections, mongoClient);
    }

    /**
     * Sets faculty selection for user and Faculty that'll be sent to Tasks database.
     * @param facultySelection
     */
    public void setFacultySelection(String facultySelection){
        this.facultySelections = facultySelection;
        mongoDB.updateDatabase("UserDatabase", "Users", this.username, "Faculty Selection", this.facultySelections, this.mongoClient);
    }

    /**
     * Sets Facilities selection for user and Facilities that'll be sent to Tasks database.
     * @param facilitiesSelection
     */
    public void setFacilitiesSelection(String facilitiesSelection){
        this.facilitiesSlection = facilitiesSelection;
        mongoDB.updateDatabase("UserDatabase", "Users", this.username, "Facilities Selection", this.facilitiesSlection, this.mongoClient);
    }

    /**
     * Sets dorm selection for user and Dorm that'll be sent to Tasks database.
     * @param dormSelection
     */
    public void setDormSelection(String dormSelection){
        this.dormSelection = dormSelection;
        mongoDB.updateDatabase("UserDatabase", "Users", this.username, "Dorm Selection", this.dormSelection, this.mongoClient);
    }

    /**
     * Sets classes for user and Classes that'll be sent to Tasks database.
     * @param selectedClass
     */
    public void setClasses(String selectedClass){
        this.classes = selectedClass;
        mongoDB.updateDatabase("UserDatabase", "Users", this.username, "Class selection", this.classes, this.mongoClient);
    }

    /**
     * Updates permission level so user can see different things based on it 
     * @param permissionLevel
     */
    public void updatePermissionLevel(String permissionLevel){
        this.permissionLevel = permissionLevel;
        mongoDB.updateDatabase("UserDatabase", "Users", this.username, "Permission Level", this.permissionLevel, this.mongoClient);
    }

    /**
     * Returns displayname
     * @return
     */
    public String getDisplayName(){
        return this.displayName;
    }

    /**
     * Returns username
     * @return
     */
    public String getUsername(){
        return this.username;
    }

    /**
     * Returns aboutme
     * @return
     */
    public String getAboutMe(){
        return this.aboutMe;
    }

    /**
     * Returns pfp
     * @return
     */
    public String getPFP(){
        return this.pfpString;
    }

    /**
     * Returns interests
     * @return
     */
    public String getInterests(){
        return this.interests;
    }

    /**
     * Returns contact information
     * @return
     */
    public String getContactInformation(){
        return this.contactInformation;
    }

    /**
     * Returns permission level
     * @return
     */
    public String getPermissionLevel(){
        return this.permissionLevel;
    }

    /**
     * Returns food selection
     * @return
     */
    public String getFoodSelection(){
        return this.foodSelections;
    }

    /**
     * Returns dorm selection
     * @return
     */
    public String getDormSelection(){
        return this.dormSelection;
    }

    /**
     * Returns faculty selection
     * @return
     */
    public String getFacultySelection(){
        return this.facultySelections;
    }

    /**
     * Returns facilities selection
     * @return
     */
    public String getFacilitiesSelection(){
        return this.facilitiesSlection;
    }

    /**
     * Returns classes selection
     * @return
     */
    public String getClassesSelection(){
        return this.classes;
    }

    /**
     * Returns photo gallery
     * @return
     */
    public ArrayList<String> getPhotoGallery(){
        return photoGallery;
    }

    /**
     * Returns catalyst notes
     * @return
     */
    public String getCatalystNotes(){
        return this.catalystNotes;
    }


    /**
     * Creates a BufferedImage given a fileLocation
     * @param fileName
     * @return
     */
    public BufferedImage createImg(String fileName){

        try{
            //Img could be read, so it returns the BufferedImage of it.
            return ImageIO.read(new File(fileName) );
        }
        catch(IOException e){

        }

        //Img couldn't be read, so a null is returned
        return null;
    }

    /**
     * Displays the PFP
     */
    public void displayPFP(){

        Graphics2D g = (Graphics2D) pfp.getGraphics();
        g.setStroke(new BasicStroke(3));
        g.setColor(Color.BLUE);
        g.drawRect(10, 10, pfp.getWidth() - 20, pfp.getHeight() - 20);

        JLabel picLabel = new JLabel(new ImageIcon(pfp));

        JPanel jPanel = new JPanel();
        jPanel.add(picLabel);

        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setSize(new Dimension(pfp.getWidth(), pfp.getHeight()));
        f.add(jPanel);
        f.setVisible(true);
    }


    /**
     * This takes in an image, and then it goes through encoding the image to 64string, and when it 
     * converts the image to 64string, then it saves it to "PFP" for database.
     * @param image
     */
    public String imageToBase64String(BufferedImage image){

        String base64String = null;

        ByteArrayOutputStream os = new ByteArrayOutputStream();

        try{
            ImageIO.write(image, "png", os);
            base64String = Base64.getEncoder().encodeToString(os.toByteArray());
        }
        catch (IOException e){
            throw new UncheckedIOException(e);
        }


        mongoDB.updateDatabase("UserDatabase", "Users", this.username, "PFP", base64String, this.mongoClient);

        return base64String;

    }

    /**
     * Converts the base64string parameter and returns it as BufferedImage.
     * @param base64String
     * @return BufferedImage now that it's been converted
     */
    public BufferedImage base64StringToImg(String base64String) {
        
        try {
            return ImageIO.read(new ByteArrayInputStream(Base64.getDecoder().decode(base64String)));
        } catch (final IOException ioe) {
            throw new UncheckedIOException(ioe);
        }
    }

    /**
     * This saves the information and will only be used when the user is being initialized
     */
    public void saveInformation(){

        MongoCollection<Document> userCollection = mongoDB.returnCollection("UserDatabase", "Users", this.mongoClient);

        //Creating new document to insert into the Users collection, so leaderboard can grab the information later
        Document document = new Document("Username", username).append("Password", password).append("Display Name", displayName)
        .append("Contact Information", this.contactInformation).append("Interests", interests).append("Food Selection", foodSelections)
        .append("Faculty Selection", facultySelections).append("Facilities Selection", facilitiesSlection).append("Dorm Selection", dormSelection)
        .append("About Me", aboutMe).append("PFP", pfpString).append("Permission Level", this.permissionLevel)
        .append("Photo Gallery", photoGallery).append("Catalyst Notes", catalystNotes);

        userCollection.insertOne(document);

    }

    /**
     * This access's the information to the user that was saved in the db, and then sets the values from the database,
     * to the variables that were made in this class.
     */
    public void accessUserInformation(String inputUsername){
        
        MongoCollection<Document> userCollection = mongoDB.returnCollection("UserDatabase", "Users", this.mongoClient);

        for(Document doc : userCollection.find() ){
            if( inputUsername.compareTo( doc.getString("Username") ) == 0){
                this.displayName = doc.getString("Display Name");
                this.username = doc.getString("Username");
                this.password = doc.getString("Password");
                this.contactInformation = doc.getString("Contact Information");
                this.interests = doc.getString("Interests");
                this.classes = doc.getString("Classes");
                this.foodSelections = doc.getString("Food Selection");
                this.facultySelections = doc.getString("Faculty Selection");
                this.facilitiesSlection = doc.getString("Facilities Selection");
                this.dormSelection = doc.getString("Dorm Selection");
                this.aboutMe = doc.getString("About Me");
                this.permissionLevel = doc.getString("Permission Level");
                this.pfpString = doc.getString("PFP");
                this.catalystNotes = doc.getString("Catalyst Notes");
                Object photos = doc.get("Photo Gallery");
                this.photoGallery = (ArrayList<String>) photos;

                
            }
            
        }


    }


}


