package com.example;

/**
 * @Date: 4-3-2023
 */


//

import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.jsoup.nodes.*;

//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

//Should update this class at midnight every day or maybe some other time but should be updated daily to see if new articles have been published
//Do not feel like overloading the website by checking every minute if something was added, maybe hourly 

public class Catalyst {

    
    
    public void readLink(String linkToParse) throws IOException{
        //Found how to use JSoup for scrapping links from: https://www.w3schools.blog/jsoup-get-links-from-html-example
        Document doc = Jsoup.connect("https://ncfcatalyst.com/archives/").get();
        Elements links = doc.select("a[href]");
        /** 
        for(Element link : links){
            System.out.println("Link: " + link.attr("href") );
        }*/
        for(int i = 0; i < 50; i++){
            System.out.println("Link: " + links.get(i).attr("href") );
            //System.out.println("Link: " + links.get(i).text() );
        }
    }


    //Remember to parse things that contain category, archives, tags, advertising, issues, staff-profiles, links not containing .com, (maybe #respond as well)
    public ArrayList<String> findArticleLinks(Element links){
        ArrayList<String> filteredLinks = new ArrayList<String>();

        return filteredLinks;
    }

    public void saveLinks(){

    }

    public void accessLinkInformation(){

    }

    public void returnLinks(){

    }


    
}
