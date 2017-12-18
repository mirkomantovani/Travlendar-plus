/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionbeans;




import entities.Preferences;
import entities.Travelmean;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import javax.ejb.EJB;

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
   
 @EJB
private PreferencesFacadeLocal preferencesFacade;
 @EJB
private BreakFacadeLocal breakFacade;
 @EJB
private TravelmeanFacadeLocal travelmeanFacade;


 //This method submits a call to the google maps distance matrix API and retrieves the info about a travel duration, matching user preferences
public long retrieveDuration(String origin, String destination,String uid) throws MalformedURLException, IOException, ParseException, org.json.simple.parser.ParseException{
   
    

   
   Preferences pref = preferencesFacade.find(Integer.parseInt(uid));
   Travelmean transports = travelmeanFacade.find(Integer.parseInt(uid));
   
   String pmoto ="";
   if(pref.getAvoidmotorways() && pref.getAvoidtolls()){
        pmoto = "&avoid=highway";
   }else if(pref.getAvoidmotorways() && !pref.getAvoidtolls()){
        pmoto = "&avoid=highway";
   }else if(!pref.getAvoidmotorways() && pref.getAvoidtolls()){
        pmoto = "&avoid=tolls";
   }else { pmoto = "";}
   
   String tway ="";
   String tway2="";
   
   if(transports.getOwnedcar() ){
       tway="&mode=driving";
   } else if(transports.getOwnedbike() ){
       tway="&mode=bicycling";
   } else if(transports.getPublictransport() ){
       tway="&mode=transit";
   } else if(transports.getWalking()){
       tway="&mode=walking";
   } if(transports.getOwnedbike() && !tway.equals("&mode=bicycling")){
       tway2=tway2.concat("|bicycling");
   } if (transports.getOwnedcar() && !tway.equals("&mode=driving")){
       tway2=tway2.concat("|driving");
   } if (transports.getPublictransport() && !tway.equals("&mode=transit")){
       tway2=tway2.concat("|transit");
   } if(transports.getWalking() && !tway.equals("&mode=walking")){
       tway2=tway2.concat("|walking"); 
   }
   
   if(pref.getMinimizecarbonfootprint() && tway.equals("&mode=driving") && !tway2.equals("")){
       tway="";
       tway2=tway2.replaceFirst("|", "&mode=");
   }
   
   if(!tway.contains("driving") && !tway2.contains("driving")){
       pmoto = "";
   }
   
   String origins;
   String destinations;
  
   origins = "origins="+origin.replaceAll(" ", "+");
   destinations = "&destinations="+destination.replaceAll(" ", "+");
   String path = "https://maps.googleapis.com/maps/api/distancematrix/json?"+origins + destinations + tway + tway2 + pmoto +"&key=AIzaSyAgeo56pmj4_foFgklzXU_NAc2trdS19x4";
    
    URLConnection connection = new URL(path).openConnection();
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
           String errore = (String)jsonObject4.get("status");
           if(!errore.equals("OK")){
               return -1;
           }
           JSONObject jsonObject5 = (JSONObject)jsonObject4.get("duration");
           JSONObject jsonObject6 = (JSONObject)jsonObject4.get("distance");
           System.out.println(jsonObject3.toString());
            
          if(tway.contains("walking") && pref.getMaxwalkingdistance() < (int) jsonObject6.get("value"))
          {
         
          return -1;
           }
          
          if(tway.contains("bicycling") && pref.getMaxcyclingdistance() < (int) jsonObject6.get("value")){
          return -1;
           }
          
          
         //TODO DOPO UNA CERTA ORA NON POSSO USARE I MEZZI if(tway.contains("transit") && !tway2.contains("|") return -1
          //                                                 else if(tway2.contains("transit"))...
          return (long) jsonObject5.get("value");
}


}
