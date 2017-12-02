/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionbeans;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import javax.ejb.Stateless;
import org.json.simple.parser.JSONParser;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;



/**
 *
 * @author matteo
 */
@Stateless
public class RouteCalculatorBean {

private int retrieveDuration() throws MalformedURLException, IOException, ParseException, org.json.simple.parser.ParseException{
    URLConnection connection = new URL("https://maps.googleapis.com/maps/api/distancematrix/json?origins=via+ungaretti,Peschiera+Borromeo&destinations=via+Turoldo,Bussero&key=AIzaSyAgeo56pmj4_foFgklzXU_NAc2trdS19x4").openConnection();
    connection.setRequestProperty("Accept-Charset", "UTF-8");
    StringBuilder responseStrBuilder;
        try (InputStream responses = connection.getInputStream()) {
            BufferedReader streamReader = new BufferedReader(new InputStreamReader(responses, "UTF-8"));
            responseStrBuilder = new StringBuilder();
            String inputStr;
            while ((inputStr = streamReader.readLine()) != null){
                responseStrBuilder.append(inputStr);
            }   }
    
    String json= responseStrBuilder.toString();
   // JSONObject jsonObject = new JSONObject(responseStrBuilder.toString());
    
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(json);
            JSONObject jb = (JSONObject) obj;
            
            
            //now read
            JSONArray jsonObject1 = (JSONArray) jb.get("rows");
            
            JSONObject jsonObject2 = (JSONObject)jsonObject1.get(0);
                 System.out.println(jsonObject2.toString());
           JSONArray jsonObject3 = (JSONArray)jsonObject2.get("elements");
           JSONObject jsonObject4 = (JSONObject)jsonObject3.get(0);
           JSONObject jsonObject5 = (JSONObject)jsonObject4.get("duration");
           System.out.println(jsonObject3.toString());
            

           return (int) jsonObject5.get("value");
      
    
  
}
}
