/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionbeans;

import entities.Preferences;
import javax.ejb.Stateless;

/**
 *
 * @author matteo
 */
@Stateless
public class showDirectionsMap {

public String queryBuilder(String origin, String destination, Preferences preferences){
    
    String path = "https://www.google.com/maps/embed/v1/directions\n" +
"  ?key=AIzaSyAgeo56pmj4_foFgklzXU_NAc2trdS19x4";
    
    origin.replaceAll(" ", "+");
    destination.replaceAll(" ","+");
    
    String start = "&origin=" + origin;
    String end = "&destination=" + destination;
    
    String tMeans = "&mode" + preferences.getTravelmean().toString();
    
    String route;
    
    if(preferences.getAvoidmotorways() == true && preferences.getAvoidtolls() == true)
    route = "&avoid=highways|tolls";
    if(preferences.getAvoidtolls() == true)
     route = "&avoid=tolls";
    if(preferences.getAvoidmotorways() == true)
        route = "&avoid=highways";
    
    String query;
    return query = path + start + end;
    
}
}
