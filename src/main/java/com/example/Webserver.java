package com.example;

/**
 * Initializes and manages an HTTP server to facilitate communication between the frontend and backend.
 * It supports various operations by mapping specific URIs to their corresponding actions,
 * such as user creation, login, and data retrieval, effectively serving as the backend's core interaction point.
 * 
 * @Date: 9-3-2023
 */

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.mongodb.MongoClient;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

//Resource used for setting up the api: https://www.codeproject.com/Tips/1040097/Create-a-Simple-Web-Server-in-Java-HTTP-Server
//2nd website: http://www.java2s.com/Code/Java/Network-Protocol/MinimalHTTPServerbyusingcomsunnethttpserverHttpServer.htm


public class Webserver{


  //Port is for the server address, and server is setup so HttpRequests can be made and replies can be made
  private UserList users = null;
  private int port;
  private HttpServer server;
  private LeaderBoard lb = null;
  public static MongoClient mongoClient = null;

  /**
   * Creates an HTTP server on the specified port and sets up context handlers for different API endpoints.
   * Each endpoint corresponds to a specific functionality, such as user management or leaderboard updates.
   *
   * @param port The port number on which the server will listen.
   * @param mongoClient The MongoDB client for database interactions.
   * @throws IOException If an I/O error occurs.
   */
  public Webserver(int port, MongoClient mongoClient) throws IOException{
    Webserver.mongoClient = mongoClient;
    this.port = port;
    this.server = HttpServer.create( new InetSocketAddress(this.port), 0);
    this.users = new UserList(mongoClient);
    this.lb = new LeaderBoard(mongoClient);

    //These are all of the contexts for the frontend to use to communicate to the backend that it needs something
    //then the backend fulfills it and replies with a message to notify frontend that the action has been completed.
    this.server.createContext("/Ping", new IndexHandler() );
    this.server.createContext("/CreateNewUser", new CreateUser( this.users ) );
    this.server.createContext("/CreateOldUser", new Login( this.users) );
    this.server.createContext("/Logout", new Logout( this.users) );
    this.server.createContext("/GetAllUserNames", new GetAllUsernames( this.users ) );
    this.server.createContext("/LikedFoods", new AddHAMFood( this.users) );
    this.server.createContext("/SelectedFaculty", new AddFaculty( this.users) );
    this.server.createContext("/LikedFacilities", new AddFacilities( this.users) );
    this.server.createContext("/SelectedDorm", new AddDorm( this.users) );
    this.server.createContext("/SelectedClasses", new AddClasses( this.users ) );
    this.server.createContext("/UpdateDisplayName", new UpdateDisplayName( this.users));
    this.server.createContext("/UpdatePassword", new UpdatePassword( this.users ) ); 
    this.server.createContext("/UploadPFP", new UploadPFP( this.users ) );
    this.server.createContext("/UpdateAboutme", new UpdateAboutMe( this.users ) );
    this.server.createContext("/UpdateContactInformation", new UpdateContactInformation( this.users ) );
    this.server.createContext("/UpdateInterests", new UpdateInterests( this.users ) );
    this.server.createContext("/UpdateCatalystNotes", new UpdateCatalyst( this.users) );
    this.server.createContext("/AddToPhotoGallery", new AddImgToPhotoGallery( this.users) );
    this.server.createContext("/ReturnUsername", new ReturnUsername( this.users ) );
    this.server.createContext("/ReturnDisplayName", new ReturnDisplayName( this.users ) );
    this.server.createContext("/ReturnPFP", new ReturnPFP( this.users ) );
    this.server.createContext("/ReturnInterests", new ReturnInterests( this.users ) );
    this.server.createContext("/ReturnAboutMe", new ReturnAboutMe( this.users ) );
    this.server.createContext("/ReturnFood", new ReturnFood( this.users ) );
    this.server.createContext("/ReturnDorm", new ReturnDorm( this.users ) );
    this.server.createContext("/ReturnClasses", new ReturnClasses( this.users ) );
    this.server.createContext("/ReturnFacilities", new ReturnFacilities( this.users ) );
    this.server.createContext("/ReturnFaculty", new ReturnFaculty( this.users ) );
    this.server.createContext("/ReturnContactInfo", new ReturnContactInformation( this.users) ); 
    this.server.createContext("/ReturnCatalystNotes", new ReturnCatalystNotes( this.users) );
    this.server.createContext("/ReturnPhotoGallery", new ReturnPhotoGallery( this.users) );
    this.server.createContext("/UpdateLeaderboard", new UpdateLeaderBoard( this.lb ) );
    this.server.createContext("/ReturnLBInfo", new ReturnLBInformation( this.lb ) ); 


    this.server.start();

  }

  /**
   * Returns the HttpServer instance.
   *
   * @return The HttpServer object.
   */
  public HttpServer getServer(){
    return this.server;
  }


  /**
   * Converts a query string into a map of key-value pairs.
   * 
   * @param query The query string to be parsed.
   * @return A map containing the parsed query parameters.
   */
  public static Map<String, String> queryToMap(String query) {
    if(query == null) {
        return null;
    }
    Map<String, String> result = new HashMap<>();
    for (String param : query.split("&")) {
        String[] entry = param.split("=");
        if (entry.length > 1) {
            result.put(entry[0], entry[1]);
        }else{
            result.put(entry[0], "");
        }
    }

    return result;
  }


}

/**
 * Handles requests to the root or index endpoint. Typically used to verify server status.
 */
class IndexHandler implements HttpHandler {
  public void handle(HttpExchange t) throws IOException {
      String response = "Response!";
      t.sendResponseHeaders(200, response.length());
      t.getResponseBody().write(response.getBytes());
      t.getResponseBody().close();
  }
}

/**
 * Retrieves all usernames registered in the system. Useful for listing users in the frontend.
 */
class GetAllUsernames implements HttpHandler{

  private UserList newUsersList;

  public GetAllUsernames(UserList users){
    this.newUsersList = users;
  }

  public void handle(HttpExchange exchange) throws IOException{

    exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
    
    Map<String, String> params = Webserver.queryToMap(exchange.getRequestURI().getQuery());

    String listUsernames = newUsersList.returnAllUserNames();

    String response = listUsernames;
    exchange.sendResponseHeaders(200, response.length());
    exchange.getResponseBody().write(response.getBytes());
    exchange.getResponseBody().close();
  }
}

/**
 * Verifies whether a given username exists within the system. Helps in user validation processes.
 */
class CheckUsers implements HttpHandler{
  
  private UserList newUsersList;

  public CheckUsers(UserList users){
    this.newUsersList = users;
  }

  public void handle(HttpExchange exchange) throws IOException{

    exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
    
    Map<String, String> params = Webserver.queryToMap(exchange.getRequestURI().getQuery());

    String username = params.get("Username");
    String checkIfUser = newUsersList.checkUsername(username);

    //The username is the token being sent back to the frontend to grab the information from the 
    //users on the website for more information requests.
    //Should probably be changed to something more secure, but that's for later.
    String response = String.valueOf(checkIfUser);
    exchange.sendResponseHeaders(200, response.length());
    exchange.getResponseBody().write(response.getBytes());
    exchange.getResponseBody().close();
  }
}

/**
 * Facilitates the creation of new user accounts. It captures user data from the frontend and persists it in the database.
 */
class CreateUser implements HttpHandler{

  private UserList newUsersList;

  public CreateUser(UserList creatingUser){
    this.newUsersList = creatingUser;
  }
  
  public void handle(HttpExchange exchange) throws IOException {

    exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
    
    Map<String, String> params = Webserver.queryToMap(exchange.getRequestURI().getQuery());

    String displayName = params.get("DisplayName");
    String username = params.get("Username");
    String password = params.get("Password");
    String contactInfo = params.get("ContactInformation");

    boolean alreadyUsername = newUsersList.createUser(displayName, username, password, contactInfo);

    //The username is the token being sent back to the frontend to grab the information from the 
    //users on the website for more information requests.
    //Should probably be changed to something more secure, but that's for later.
    String response = String.valueOf(alreadyUsername);
    exchange.sendResponseHeaders(200, response.length());
    exchange.getResponseBody().write(response.getBytes());
    exchange.getResponseBody().close();
  }
  
}

/**
 * Handles user login requests by verifying provided credentials against stored data.
 */
class Login implements HttpHandler{

  private UserList userList;

  public Login(UserList users){
    this.userList = users;
  }

  public void handle(HttpExchange exchange) throws IOException {

    exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");

    Map<String, String> params = Webserver.queryToMap(exchange.getRequestURI().getQuery());

    String username = params.get("Username");
    String password = params.get("Password");

    String response = userList.checkLogin(username, password);

    //The username is the token being sent back to the frontend to grab the information from the 
    //users on the website for more information requests.
    //Should probably be changed to something more secure, but that's for later.
    exchange.sendResponseHeaders(200, response.length());
    exchange.getResponseBody().write(response.getBytes());
    exchange.getResponseBody().close();
  }

}


/**
 * Processes user logout requests. It can be used to clear session data or perform other cleanup tasks.
 */
class Logout implements HttpHandler{

  private UserList userList;

  public Logout(UserList users){
    this.userList = users;
  }

  public void handle(HttpExchange exchange) throws IOException {

    exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");

    Map<String, String> params = Webserver.queryToMap(exchange.getRequestURI().getQuery());
    
    String username = params.get("Username");
    User toRemoveUser = userList.accessUser(username);
    userList.removeUser(toRemoveUser);

    String response = "The user has been logged out";
    exchange.sendResponseHeaders(200, response.length());
    exchange.getResponseBody().write(response.getBytes());
    exchange.getResponseBody().close();
  } 
}


/**
 * Handles the addition of preferred foods for a user. This can be part of user preferences or profile settings.
 */
class AddHAMFood implements HttpHandler{

  private UserList userList;

  public AddHAMFood(UserList users){
    this.userList = users;
  }

  public void handle(HttpExchange exchange) throws IOException{

    exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");

    Map<String, String> params = Webserver.queryToMap(exchange.getRequestURI().getQuery());

    String username = params.get("Username");
    String likedFoods = params.get("LikedFoods");
    User user = userList.accessUser(username);

    HAM ham = new HAM(user, userList.returnMongoClient() );
    ham.addFood(likedFoods);
    ham.completeTask();
    user.setFoodSelection(likedFoods);

    String response = "Response";
    exchange.sendResponseHeaders(200, response.length());
    exchange.getResponseBody().write(response.getBytes());
    exchange.getResponseBody().close();
  }
}


/**
 * Allows users to select their preferred faculty. This data can be used for customized content delivery or notifications.
 */
class AddFaculty implements HttpHandler{

  private UserList userList;

  public AddFaculty(UserList users){
    this.userList = users;
  }

  public void handle(HttpExchange exchange) throws IOException{

    exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");

    Map<String, String> params = Webserver.queryToMap(exchange.getRequestURI().getQuery());

    String username = params.get("Username");
    String selectedFaculty = params.get("SelectedFaculty");
    User user = userList.accessUser(username);

    Faculty faculty = new Faculty(user);
    faculty.addFaculty(selectedFaculty);
    faculty.completeTask();
    user.setFacultySelection(selectedFaculty);

    String response = "Response";
    exchange.sendResponseHeaders(200, response.length());
    exchange.getResponseBody().write(response.getBytes());
    exchange.getResponseBody().close();
  }
}

/**
 * Handles the selection of classes by a user. This could be for scheduling purposes or to indicate interest.
 */
class AddClasses implements HttpHandler{

  private UserList userList;

  public AddClasses(UserList users){
    this.userList = users;
  }

  public void handle(HttpExchange exchange) throws IOException{

    exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");

    Map<String, String> params = Webserver.queryToMap(exchange.getRequestURI().getQuery());

    String username = params.get("Username");
    String selectedClasses = params.get("SelectedClasses");
    User user = userList.accessUser(username);

    Classes classes = new Classes(user, userList.returnMongoClient() );
    classes.addClasses(selectedClasses);
    classes.completeTask();
    user.setClasses(selectedClasses);

    String response = "Response";
    exchange.sendResponseHeaders(200, response.length());
    exchange.getResponseBody().write(response.getBytes());
    exchange.getResponseBody().close();
  }
}

/**
 * Manages user preferences related to facilities. This could involve booking facilities or expressing interest.
 */
class AddFacilities implements HttpHandler{

  private UserList userList;

  public AddFacilities(UserList users){
    this.userList = users;
  }

  public void handle(HttpExchange exchange) throws IOException{

    exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");

    Map<String, String> params = Webserver.queryToMap(exchange.getRequestURI().getQuery());

    String username = params.get("Username");
    String likedFacilities = params.get("LikedFacilities");
    User user = userList.accessUser(username);

    Facilities facilities = new Facilities(user, userList.returnMongoClient() );
    facilities.addFacilities(likedFacilities);
    facilities.completeTask();
    user.setFacilitiesSelection(likedFacilities);

    String response = "Response";
    exchange.sendResponseHeaders(200, response.length());
    exchange.getResponseBody().write(response.getBytes());
    exchange.getResponseBody().close();
  }
}

/**
 * Enables users to choose or change their dormitory preferences. Useful for housing arrangements.
 */
class AddDorm implements HttpHandler{

  private UserList userList;

  public AddDorm(UserList users){
    this.userList = users;
  }

  public void handle(HttpExchange exchange) throws IOException{

    exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");

    Map<String, String> params = Webserver.queryToMap(exchange.getRequestURI().getQuery());

    String username = params.get("Username");
    String selectedDorm = params.get("SelectedDorm");
    User user = userList.accessUser(username);

    Dorm dorm = new Dorm(user, userList.returnMongoClient() );
    dorm.addDorm(selectedDorm);
    dorm.completeTask();
    user.setDormSelection(selectedDorm);

    String response = "Response";
    exchange.sendResponseHeaders(200, response.length());
    exchange.getResponseBody().write(response.getBytes());
    exchange.getResponseBody().close();
  }
}

/**
 * Updates a user's display name. This could be part of a profile update feature.
 */
class UpdateDisplayName implements HttpHandler{
  
  private UserList userList;

  public UpdateDisplayName(UserList users){
    this.userList = users;
  }

  public void handle(HttpExchange exchange) throws IOException{

    exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");

    Map<String, String> params = Webserver.queryToMap(exchange.getRequestURI().getQuery());

    String username = params.get("Username");
    String newDisplayName = params.get("NewDisplayName");
    User user = userList.accessUser(username);
    user.updateDisplayName(newDisplayName);

    String response = "User's display name has been updated";
    exchange.sendResponseHeaders(200, response.length());
    exchange.getResponseBody().write(response.getBytes());
    exchange.getResponseBody().close();
  }
}


/**
 * Allows users to change their password. A critical feature for account security.
 */
class UpdatePassword implements HttpHandler{
  
  private UserList userList;

  public UpdatePassword(UserList users){
    this.userList = users;
  }

  public void handle(HttpExchange exchange) throws IOException{

    exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");

    Map<String, String> params = Webserver.queryToMap(exchange.getRequestURI().getQuery());

    String username = params.get("Username");
    String newPassword = params.get("NewPassword");
    User user = userList.accessUser(username);
    user.updatePassword(newPassword);

    String response = "User's password has been updated";
    exchange.sendResponseHeaders(200, response.length());
    exchange.getResponseBody().write(response.getBytes());
    exchange.getResponseBody().close();
  }
}


/**
 * Supports uploading or changing a user's profile picture. Enhances user profile customization.
 */
class UploadPFP implements HttpHandler{
  
  private UserList userList;

  public UploadPFP(UserList users){
    this.userList = users;
  }

  public void handle(HttpExchange exchange) throws IOException{

    exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");

    Map<String, String> params = Webserver.queryToMap(exchange.getRequestURI().getQuery());

    String username = params.get("Username");
    String newPFPFile = params.get("NewPFPFile");
    User user = userList.accessUser(username);
    user.uploadPFP(newPFPFile);

    String response = "User's pfp has been uploaded";
    exchange.sendResponseHeaders(200, response.length());
    exchange.getResponseBody().write(response.getBytes());
    exchange.getResponseBody().close();
  }
}

/**
 * Updates the "About Me" section of a user's profile. Allows for personalized user bios.
 */
class UpdateAboutMe implements HttpHandler{
  
  private UserList userList;

  public UpdateAboutMe(UserList users){
    this.userList = users;
  }

  public void handle(HttpExchange exchange) throws IOException{

    exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");

    Map<String, String> params = Webserver.queryToMap(exchange.getRequestURI().getQuery());

    String username = params.get("Username");
    String newAboutMe = params.get("NewAboutme");
    User user = userList.accessUser(username);
    user.updateAboutMe(newAboutMe);

    String response = "User's About me has been updated";
    exchange.sendResponseHeaders(200, response.length());
    exchange.getResponseBody().write(response.getBytes());
    exchange.getResponseBody().close();
  }
}

/**
 * Updates a user's contact information. Essential for keeping user profiles up-to-date.
 */
class UpdateContactInformation implements HttpHandler{
  
  private UserList userList;

  public UpdateContactInformation(UserList users){
    this.userList = users;
  }

  public void handle(HttpExchange exchange) throws IOException{

    exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");

    Map<String, String> params = Webserver.queryToMap(exchange.getRequestURI().getQuery());

    String username = params.get("Username");
    String newContactInfo = params.get("NewContactInformation");
    User user = userList.accessUser(username);
    user.updateContactInfo(newContactInfo);

    String response = "Response";
    exchange.sendResponseHeaders(200, response.length());
    exchange.getResponseBody().write(response.getBytes());
    exchange.getResponseBody().close();
  }
}

/**
 * Facilitates adding messages or notes related to the Catalyst project. Could be used for project collaboration or notes sharing.
 */
class UpdateCatalyst implements HttpHandler{

  private UserList userList;

  public UpdateCatalyst(UserList users){
    this.userList = users;
  }

  public void handle(HttpExchange exchange) throws IOException{

    exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");

    Map<String, String> params = Webserver.queryToMap(exchange.getRequestURI().getQuery());

    String token = params.get("Username");
    String notes = params.get("Notes");
    User user = userList.accessUser(token);
    user.updateCatalystNote(notes);

    String response = notes;
    exchange.sendResponseHeaders(200, response.length());
    exchange.getResponseBody().write(response.getBytes());
    exchange.getResponseBody().close();

  }
}

/**
 * Allows users to update their list of interests. Can be used to tailor content or recommendations.
 */
class UpdateInterests implements HttpHandler{

  private UserList userList;

  public UpdateInterests(UserList users){
    this.userList = users;
  }

  public void handle(HttpExchange exchange) throws IOException{

    exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");

    Map<String, String> params = Webserver.queryToMap(exchange.getRequestURI().getQuery());

    String token = params.get("Username");
    String interest = params.get("Interest");
    User user = userList.accessUser(token);
    user.updateInterests(interest);


    String response = interest;
    exchange.sendResponseHeaders(200, response.length());
    exchange.getResponseBody().write(response.getBytes());
    exchange.getResponseBody().close();
  }
}

/**
 * Returns a user's username. This might be used for account management or display purposes.
 */
class ReturnUsername implements HttpHandler{

  private UserList userList;

  public ReturnUsername(UserList users){
    this.userList = users;
  }

  public void handle(HttpExchange exchange) throws IOException{

    exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");

    Map<String, String> params = Webserver.queryToMap(exchange.getRequestURI().getQuery());

    String token = params.get("Username");

    User user = userList.accessUser(token);
    String username = user.getUsername();

    String response = username;
    exchange.sendResponseHeaders(200, response.length());
    exchange.getResponseBody().write(response.getBytes());
    exchange.getResponseBody().close();
  }
}

/**
 * Returns a user's display name. Useful for personalized greetings or displays in the UI.
 */
class ReturnDisplayName implements HttpHandler{

  private UserList userList;

  public ReturnDisplayName(UserList users){
    this.userList = users;
  }

  public void handle(HttpExchange exchange) throws IOException{

    exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");

    Map<String, String> params = Webserver.queryToMap(exchange.getRequestURI().getQuery());

    String token = params.get("Username");

    User user = userList.accessUser(token);
    String displayName = user.getDisplayName();

    String response = displayName;
    exchange.sendResponseHeaders(200, response.length());
    exchange.getResponseBody().write(response.getBytes());
    exchange.getResponseBody().close();
  }
}

/**
 * Handles requests to retrieve a user's profile picture. Enhances personalization of user interfaces.
 */
class ReturnPFP implements HttpHandler{

  private UserList userList;

  public ReturnPFP(UserList users){
    this.userList = users;
  }

  public void handle(HttpExchange exchange) throws IOException{

    exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");

    Map<String, String> params = Webserver.queryToMap(exchange.getRequestURI().getQuery());

    String token = params.get("Username");

    User user = userList.accessUser(token);
    String pfp = user.getPFP();

    String response = pfp;
    exchange.sendResponseHeaders(200, response.length());
    exchange.getResponseBody().write(response.getBytes() );
    exchange.getResponseBody().close();
  }
}

/**
 * Returns a user's photo gallery. Allows users to share and display a collection of images.
 */
class ReturnPhotoGallery implements HttpHandler{

  private UserList userList;

  public ReturnPhotoGallery(UserList users){
    this.userList = users;
  }

  public void handle(HttpExchange exchange) throws IOException{
    exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");

    Map<String, String> params = Webserver.queryToMap(exchange.getRequestURI().getQuery());

    String token = params.get("Username");

    User user = userList.accessUser(token);
    ArrayList<String> photoGallery = user.getPhotoGallery();
    Object photos = (Object) photoGallery;

    String response = String.valueOf(photos);
    exchange.sendResponseHeaders(200, response.length());
    exchange.getResponseBody().write(response.getBytes() );
    exchange.getResponseBody().close();

  }
}

/**
 * Returns the Catalyst project notes associated with a user. Useful for sharing project insights or updates.
 */
class ReturnCatalystNotes implements HttpHandler{

  private UserList userList;

  public ReturnCatalystNotes(UserList users){
    this.userList = users;
  }

  public void handle(HttpExchange exchange) throws IOException{
    exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");

    Map<String, String> params = Webserver.queryToMap(exchange.getRequestURI().getQuery());

    String token = params.get("Username");
    User user = userList.accessUser(token);
    String notes = user.getCatalystNotes();

    String response = notes;
    exchange.sendResponseHeaders(200, response.length());
    exchange.getResponseBody().write(response.getBytes());
    exchange.getResponseBody().close();
  }
}

/**
 * Returns the "About Me" section of a user's profile. Provides insight into the user's personal or professional background.
 */
class ReturnAboutMe implements HttpHandler{

  private UserList userList;

  public ReturnAboutMe(UserList users){
    this.userList = users;
  }

  public void handle(HttpExchange exchange) throws IOException{

    exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");

    Map<String, String> params = Webserver.queryToMap(exchange.getRequestURI().getQuery());

    String token = params.get("Username");

    User user = userList.accessUser(token);
    String aboutMe = user.getAboutMe();

    String response = aboutMe;
    exchange.sendResponseHeaders(200, response.length());
    exchange.getResponseBody().write(response.getBytes());
    exchange.getResponseBody().close();
  }
}

/**
 * Retrieves a user's listed interests. Can inform content recommendations or community connections.
 */
class ReturnInterests implements HttpHandler{

  private UserList userList;

  public ReturnInterests(UserList users){
    this.userList = users;
  }

  public void handle(HttpExchange exchange) throws IOException{

    exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");

    Map<String, String> params = Webserver.queryToMap(exchange.getRequestURI().getQuery());

    String token = params.get("Username");

    User user = userList.accessUser(token);
    String interests = user.getInterests();

    String response = interests;
    exchange.sendResponseHeaders(200, response.length());
    exchange.getResponseBody().write(response.getBytes());
    exchange.getResponseBody().close();
  }
}

/**
 * Returns the user's preferred food selections. May influence dining options or nutritional suggestions.
 */
class ReturnFood implements HttpHandler{

  private UserList userList;

  public ReturnFood(UserList users){
    this.userList = users;
  }

  public void handle(HttpExchange exchange) throws IOException{ 

    exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");

    Map<String, String> params = Webserver.queryToMap(exchange.getRequestURI().getQuery());

    String token = params.get("Username");
    User user = userList.accessUser(token);
    String food = user.getFoodSelection();

    String response = food;
    exchange.sendResponseHeaders(200, response.length());
    exchange.getResponseBody().write(response.getBytes());
    exchange.getResponseBody().close();

  }
}

/**
 * Retrieves the user's dormitory selection. Important for housing arrangements and community building.
 */
class ReturnDorm implements HttpHandler{

  private UserList userList;

  public ReturnDorm(UserList users){
    this.userList = users;
  }

  public void handle(HttpExchange exchange) throws IOException{

    exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
    
    Map<String, String> params = Webserver.queryToMap(exchange.getRequestURI().getQuery());

    String token = params.get("Username");
    User user = userList.accessUser(token);
    String dorm = user.getDormSelection();

    String response = dorm;
    exchange.sendResponseHeaders(200, response.length());
    exchange.getResponseBody().write(response.getBytes());
    exchange.getResponseBody().close();
  }
}

/**
 * Returns the classes selected by the user. Key for academic scheduling and course management.
 */
class ReturnClasses implements HttpHandler{

  private UserList userList;

  public ReturnClasses(UserList users){
    this.userList = users;
  }

  public void handle(HttpExchange exchange) throws IOException{

    exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
    
    Map<String, String> params = Webserver.queryToMap(exchange.getRequestURI().getQuery());

    String token = params.get("Username");
    User user = userList.accessUser(token);
    String classes = user.getClassesSelection();

    String response = classes;
    exchange.sendResponseHeaders(200, response.length());
    exchange.getResponseBody().write(response.getBytes());
    exchange.getResponseBody().close();
  }
}

/**
 * Retrieves the user's facilities preferences. Can assist in facilities management and reservation systems.
 */
class ReturnFacilities implements HttpHandler{

  private UserList userList;

  public ReturnFacilities(UserList users){
    this.userList = users;
  }

  public void handle(HttpExchange exchange) throws IOException{

    exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
    
    Map<String, String> params = Webserver.queryToMap(exchange.getRequestURI().getQuery());
    
    String token = params.get("Username");
    User user = userList.accessUser(token);
    String facilities = user.getFacilitiesSelection();

    String response = facilities;
    exchange.sendResponseHeaders(200, response.length());
    exchange.getResponseBody().write(response.getBytes());
    exchange.getResponseBody().close();
  }
}

/**
 * Returns the faculty selected by the user. Useful for advising, mentorship, or personalized academic content.
 */
class ReturnFaculty implements HttpHandler{

  private UserList userList;
  
  public ReturnFaculty(UserList users){
    this.userList = users;
  }

  public void handle(HttpExchange exchange) throws IOException{

    exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");

    Map<String, String> params = Webserver.queryToMap(exchange.getRequestURI().getQuery());

    String token = params.get("Username");
    User user = userList.accessUser(token);
    String faculty = user.getFacultySelection();

    String response = faculty;
    exchange.sendResponseHeaders(200, response.length());
    exchange.getResponseBody().write(response.getBytes());
    exchange.getResponseBody().close();
  }
}

/**
 * Retrieves a user's contact information. Essential for communication and emergency contact purposes.
 */
class ReturnContactInformation implements HttpHandler{

  private UserList userList;
  
  public ReturnContactInformation(UserList users){
    this.userList = users;
  }

  public void handle(HttpExchange exchange) throws IOException{

    exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");

    Map<String, String> params = Webserver.queryToMap(exchange.getRequestURI().getQuery());

    String token = params.get("Username");
    User user = userList.accessUser(token);
    String contactInfo = user.getContactInformation();

    String response = contactInfo;
    exchange.sendResponseHeaders(200, response.length());
    exchange.getResponseBody().write(response.getBytes());
    exchange.getResponseBody().close();
  }
}

/**
 * Handles requests to add an image to a user's photo gallery. This feature allows users to personalize
 * their profiles by adding images to their gallery, enhancing the social aspect of the application.
 * The image details are received from the frontend, and the specified image is then added to the user's
 * photo gallery in the backend.
 */
class AddImgToPhotoGallery implements HttpHandler{

  private UserList userList;

  public AddImgToPhotoGallery(UserList users){
    this.userList = users;
  }

  public void handle(HttpExchange exchange) throws IOException{

    Map<String, String> params = Webserver.queryToMap(exchange.getRequestURI().getQuery());

    String token = params.get("Username");
    String newImg = params.get("New Image");
    User user = userList.accessUser(token);
    user.addImgToPhotos(newImg);

    String response = token;
    exchange.sendResponseHeaders(200, response.length());
    exchange.getResponseBody().write(response.getBytes());
    exchange.getResponseBody().close();
  }
}


/**
 * Triggers an update to the leaderboard based on user activities. Key for gamification or tracking progress.
 */
class UpdateLeaderBoard implements HttpHandler{

  private LeaderBoard lb;

  public UpdateLeaderBoard( LeaderBoard leaderBoard){
    this.lb = leaderBoard;
  }

  public void handle(HttpExchange exchange) throws IOException{

    exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
    
    lb.getTasksInformation();

    String response = "Response";
    exchange.sendResponseHeaders(200, response.length());
    exchange.getResponseBody().write(response.getBytes());
    exchange.getResponseBody().close();
  }
}

/**
 * Returns leaderboard information. Enables competition and progress tracking among users.
 */
class ReturnLBInformation implements HttpHandler{

  private LeaderBoard lb;

  public ReturnLBInformation( LeaderBoard leaderboard){
    this.lb = leaderboard;
  }

  public void handle(HttpExchange exchange) throws IOException{

    exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");

    ArrayList<ArrayList<String>> lbInfo = lb.returnLists();

    ArrayList<String> responseList = new ArrayList<>();

    for( ArrayList<String> arrString : lbInfo){
      String inputResponse = "";
      for(int i = 0; i < 2; i++){
        if(i == 1){
          inputResponse += ":";
        }
        inputResponse += arrString.get(i);
      }
      responseList.add(inputResponse);
    }

    String response = ""; 

    for(String str : responseList){
      response += str + ",";
    }

    exchange.sendResponseHeaders(200, response.length());
    exchange.getResponseBody().write(response.getBytes());
    exchange.getResponseBody().close();
    
  }
}