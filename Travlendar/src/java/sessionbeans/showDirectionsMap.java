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
import javax.ejb.EJB;
import javax.ejb.Stateless;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author matteo
 */
@Stateless
public class showDirectionsMap {
    
@EJB
private PreferencesFacadeLocal preferencesFacade;
@EJB
private BreakFacadeLocal breakFacade;
@EJB
private TravelmeanFacadeLocal travelmeanFacade;

public String queryBuilder(String origin, String destination, String uid ) throws ParseException, IOException{
    
   
   Preferences pref = preferencesFacade.find(Integer.parseInt(uid));
   Travelmean transports = travelmeanFacade.find(Integer.parseInt(uid));
   
   String pmoto ="";
   if(pref.getAvoidmotorways() && pref.getAvoidtolls()){
        pmoto = "&avoid=tolls";
   }else if(pref.getAvoidmotorways()){
        pmoto = "&avoid=highway";
   }else if( pref.getAvoidtolls()){
        pmoto = "&avoid=tolls";
   }else { pmoto = "";}
   
   String tway ="";
   String tway2="";
   String twayf="";
   
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
   
   String carWasTrue="";
   
     if(pref.getMinimizecarbonfootprint() && tway.equals("&mode=driving") && !tway2.equals("")){
       tway="";
       tway2=tway2.replaceFirst("|", "&mode=");
       carWasTrue="&mode=driving";
       
   }
   
   String origins;
   String destinations;
  
   origins = "origin="+origin.replaceAll(" ", "+");
   destinations = "&destination="+destination.replaceAll(" ", "+");
   
   long driveD=Integer.MAX_VALUE;
   long walkD=Integer.MAX_VALUE;
   long bikeD=Integer.MAX_VALUE;
   long pubD=Integer.MAX_VALUE;
   long comparator = 0;
   
    if(tway.contains("driving") || tway2.contains("driving")){
        driveD = this.calculateDuration(origin, destination, "&mode=driving", uid);
   }
    if(tway.contains("walking") || tway2.contains("walking"))
         walkD = this.calculateDuration(origin, destination, "&mode=walking", uid);
    if(tway.contains("bicycling") || tway2.contains("bicycling"))
        bikeD=this.calculateDuration(origin, destination, "&mode=bicycling", uid);
    if(tway.contains("transit")|| tway2.contains("transit"))
        pubD = this.calculateDuration(origin, destination, "&mode=transit", uid);
    
    if(driveD<walkD){
        twayf="&mode=driving";
        comparator=driveD;
        }
    else{
        twayf="&mode=walking";
        comparator=walkD;
    }
    
    if(comparator>bikeD && bikeD!=-1){
        twayf="&mode=bicycling";
        comparator=bikeD;
    }
    
    if(comparator>pubD){
        twayf="&mode=transit";
    }
    
       if(!twayf.contains("driving")){
       pmoto = "";
   }
        
    String path = "https://www.google.com/maps/embed/v1/directions?"; 
   
    String query;
    
    if(pref.getMaxwalkingdistance() < this.calculateDistance(origin, destination, "&mode=walking", uid) && twayf.equals("&mode=walking")){
        if(!carWasTrue.equals("&mode=driving"))
            return "The target is too far according to maxWalkingDistance parameter";
        else
            twayf = carWasTrue;
    }
    
    if(pref.getMaxcyclingdistance() < this.calculateDistance(origin, destination, "&mode=bicycling", uid) && twayf.contains("bicycling")){
        if(!transports.getOwnedcar() && !transports.getPublictransport() && !transports.getWalking())
           return "The target is too far according to maxCyclingDistance parameter";
        else if(transports.getOwnedcar())
            twayf="&mode=driving";
        else if(transports.getPublictransport())
            twayf="&mode=transit";
        else if(transports.getWalking() && pref.getMaxwalkingdistance() > this.calculateDistance(origin, destination, "&mode=walking", uid))
            twayf="&mode=transit";
        else
            return "No routes available, modify preferences";
    }
   
   
    return query = path + origins + destinations + twayf + pmoto + "&key=AIzaSyAgeo56pmj4_foFgklzXU_NAc2trdS19x4" ;
    
}

private long calculateDuration(String origin, String destination, String mode,String uid) throws ParseException, IOException{
    
    Preferences pref = preferencesFacade.find(Integer.parseInt(uid));
    
    String pmoto ="";
   if(pref.getAvoidmotorways() && pref.getAvoidtolls()){
        pmoto = "&avoid=highway";
   }else if(pref.getAvoidmotorways() && !pref.getAvoidtolls()){
        pmoto = "&avoid=highway";
   }else if(!pref.getAvoidmotorways() && pref.getAvoidtolls()){
        pmoto = "&avoid=tolls";
   }else { pmoto = "";}
   
   if(!mode.equals("&mode=driving")){
       pmoto="";
   }
   
   String origins;
   String destinations;
  
   origins = "origins="+origin.replaceAll(" ", "+");
   destinations = "&destinations="+destination.replaceAll(" ", "+");
   String path = "https://maps.googleapis.com/maps/api/distancematrix/json?"+origins + destinations + mode + pmoto +"&key=AIzaSyAgeo56pmj4_foFgklzXU_NAc2trdS19x4";
    
    URLConnection connection = new URL("https://maps.googleapis.com/maps/api/distancematrix/json?"+origins + destinations + mode + pmoto +"&key=AIzaSyAgeo56pmj4_foFgklzXU_NAc2trdS19x4").openConnection();
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
           if(errore.equals("ZERO_RESULTS")){
               return -1;
           }
           JSONObject jsonObject5 = (JSONObject)jsonObject4.get("duration");
           JSONObject jsonObject6 = (JSONObject)jsonObject4.get("distance");
           System.out.println(jsonObject3.toString());
            
           
          
         //TODO DOPO UNA CERTA ORA NON POSSO USARE I MEZZI if(tway.contains("transit") && !tway2.contains("|") return -1
          //                                                 else if(tway2.contains("transit"))...
          return (long) jsonObject5.get("value");
   
}

private long calculateDistance(String origin,String destination, String mode, String uid) throws MalformedURLException, IOException, ParseException{
    
   
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
   
   if(!mode.equals("&mode=driving")){
       pmoto="";
   }
   
   String origins;
   String destinations;
  
   origins = "origins="+origin.replaceAll(" ", "+");
   destinations = "&destinations="+destination.replaceAll(" ", "+");
   String path = "https://maps.googleapis.com/maps/api/distancematrix/json?"+origins + destinations + mode + pmoto +"&key=AIzaSyAgeo56pmj4_foFgklzXU_NAc2trdS19x4";
    
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
           if(errore.equals("ZERO_RESULTS")){
               return -1;
           }
           JSONObject jsonObject5 = (JSONObject)jsonObject4.get("duration");
           JSONObject jsonObject6 = (JSONObject)jsonObject4.get("distance");
           System.out.println(jsonObject3.toString());
   
          
         //TODO DOPO UNA CERTA ORA NON POSSO USARE I MEZZI if(tway.contains("transit") && !tway2.contains("|") return -1
          //                                                 else if(tway2.contains("transit"))...
          return (long) jsonObject6.get("value");
}
}
