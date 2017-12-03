/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionbeans;

import entities.Break;
import entities.Preferences;
import entities.Travelmean;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author matteo
 */
@Stateless
public class showDirectionsMap {
    
@EJB
private PreferencesFacadeLocal preferencesFacade;
private BreakFacadeLocal breakFacade;
private TravelmeanFacadeLocal travelmeanFacade;

public String queryBuilder(String origin, String destination ){
    
       HttpServletRequest request = (HttpServletRequest) (FacesContext.getCurrentInstance().getExternalContext().getRequest());
   
   HttpSession session=request.getSession();
   
   String uid = session.getAttribute("uid").toString();
   
   Preferences pref = preferencesFacade.find(Integer.parseInt(uid));
   Break breaks = breakFacade.find(Integer.parseInt(uid));
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
   
   if(!transports.getOwnedcar() ){
       tway="&mode=driving";
   } else if(transports.getOwnedbike() ){
       tway="&mode=bicycling";
   } else if(transports.getPublictransport() ){
       tway="&mode=transit";
   } else if(transports.getWalking()){
       tway="&mode=walking";
   } if(transports.getOwnedbike() && !tway.equals("&mode=bicycling")){
       tway2.concat("|bicycling");
   } if (transports.getOwnedcar() && !tway.equals("&mode=driving")){
       tway2.concat("|driving");
   } if (transports.getPublictransport() && !tway.equals("&mode=transit")){
       tway2.concat("|transit");
   } if(transports.getWalking() && !tway.equals("&mode=walking")){
       tway2.concat("|walking"); 
   }
   
   String origins;
   String destinations;
  
   origins = "origins="+origin.replaceAll(" ", "+");
   destinations = "&destinations="+destination.replaceAll(" ", "+");
   
    
    
    String path = "https://www.google.com/maps/embed/v1/directions" +
"?key=AIzaSyAgeo56pmj4_foFgklzXU_NAc2trdS19x4";
    
 
    String route;
    String query;
    return query = path + origins + destination + tway + tway2 + pmoto;
    
}
}
