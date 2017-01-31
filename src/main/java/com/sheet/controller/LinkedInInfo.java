package com.sheet.controller;

import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.net.HttpURLConnection;
import java.net.URL;
import oauth.signpost.OAuth;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
import oauth.signpost.basic.DefaultOAuthConsumer;
import oauth.signpost.basic.DefaultOAuthProvider;
public class LinkedInInfo
{
	public static void main(String[] args) throws Exception {
    	OAuthConsumer consumer = new DefaultOAuthConsumer(
                "81jj97xnmwzr02",
                "ikdMlPaXXglI923l"
                );

    OAuthProvider provider = new DefaultOAuthProvider(
                "https://api.linkedin.com/uas/oauth/requestToken",
                "https://api.linkedin.com/uas/oauth/accessToken",
                "https://api.linkedin.com/uas/oauth/authorize");


        System.out.println("Fetching request token from LinkedIn...");

        // we do not support callbacks, thus pass OOB
       
 String authUrl = provider.retrieveRequestToken(consumer,OAuth.OUT_OF_BAND);

 
       System.out.println("Request token: " + consumer.getToken());
   
     System.out.println("Token secret: " + consumer.getTokenSecret());

 
       System.out.println("Now visit:\n" + authUrl
                + "\n... and grant this app authorization");

        System.out.println("Enter the PIN code and hit ENTER when you're done:");


        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
 
       String pin = br.readLine();


        System.out.println("Fetching access token from LinkedIn...");


        provider.retrieveAccessToken(consumer,pin);


        System.out.println("Access token: " + consumer.getToken());

        System.out.println("Token secret: " + consumer.getTokenSecret());

 
     /*  URL url = new URL("http://api.linkedin.com/v1/people/~:(id,first-name,last-name,picture-url,headline)");*/
        URL url = new URL("http://api.linkedin.com/v1/people/~/picture-urls::(original)");
      /* URL url = new URL("https://api.linkedin.com/v1/people/~:(first-name,last-name,email-address,picture-url,public-profile-url,summary,industry)?format=json");*/

        HttpURLConnection request = (HttpURLConnection) url.openConnection();

  
      consumer.sign(request);


        System.out.println("Sending request to LinkedIn...");
 
       request.connect();
 
       String responseBody = convertStreamToString(request.getInputStream());

        
        System.out.println("Response: " + request.getResponseCode() + " "
                + request.getResponseMessage() + "\n\n" + responseBody);
    }
    
   
 // Stolen liberally from http://www.kodejava.org/examples/266.html
    
public static String convertStreamToString(InputStream is) {
        /*
         * To convert the InputStream to String we use the BufferedReader.readLine()
         * method. We iterate until the BufferedReader return null which means
         * there's no more data to read. Each line will appended to a StringBuilder
         * and returned as String.
         */
        
BufferedReader reader = new BufferedReader(new InputStreamReader(is));
  
      StringBuilder sb = new StringBuilder();
 

        String line = null;
 
       try {
            while ((line = reader.readLine()) != null) {
 
               sb.append(line + "\n");
 

           }
        } 
catch (IOException e) {
            
e.printStackTrace();
 
       } finally {
     
       try {
         
       is.close();
    
        } catch (IOException e) {
 
               e.printStackTrace();
 
           }
        }
 
        return sb.toString();
   
 }
}