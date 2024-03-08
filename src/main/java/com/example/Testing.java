
package com.example;

/**
 * Mostly just used to create Webserver then run it
 * @Date: 15-3-23
 */



//Resource for img to base64string and base64string to img: https://stackoverflow.com/questions/7178937/java-bufferedimage-to-png-format-base64-string#:~:text=Here%20is%20the%20code%20that,os)%3B%20String%20result%20%3D%20Base64.
//Resource for MongoDB: https://www.mongodb.com/docs/manual/introduction/
//Resource for Webserver: http://www.java2s.com/Code/Java/Network-Protocol/MinimalHTTPServerbyusingcomsunnethttpserverHttpServer.htm


import java.io.IOException;

import javax.script.ScriptException;

import io.github.cdimascio.dotenv.Dotenv;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;


public class Testing {
    
  
    public static void main(String[] args) throws ScriptException, IOException{

        Dotenv dotenv = Dotenv.load();
        String mongodbUrl = dotenv.get("MONGODB_URL");

        MongoClientURI uri = new MongoClientURI(mongodbUrl);
        MongoClient mongoClient = new MongoClient(uri);

        //System.out.println(mongoClient);
        //System.exit(1);

        //This starts the webserver
        Webserver server = new Webserver(80, mongoClient);

    }

}

