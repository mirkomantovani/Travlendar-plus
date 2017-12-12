/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionbeans;

import entities.Break;
import entities.Meeting;
import entities.Warning;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author matteo
 */
@Stateless
public class ConflictCheckerBean {

    @EJB
    private MeetingFacadeLocal meetingFacade;
    @EJB
    private RouteCalculatorBean nav;
    @EJB
    private BreakFacadeLocal breakFacade;
    
    @EJB
    private WarningFacadeLocal warningFacade;
    
    //returns true if m is feasible, false otherwise
    public boolean CheckAllConflicts(Meeting m){
        
        ArrayList<Break> noReschedulableBreaks = new ArrayList<Break>();
        ArrayList<Meeting> meetings = new ArrayList<Meeting>();
        Warning w = new Warning();
        
        try {
            meetings = CheckMeetingOverlaps(m);
        } catch (IOException ex) {
            Logger.getLogger(ConflictCheckerBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(ConflictCheckerBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (org.json.simple.parser.ParseException ex) {
            Logger.getLogger(ConflictCheckerBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        noReschedulableBreaks = checkReschedule(m.getMeetingPK().getUid());
        
        String meetingsField="";
        String breaksField="";
        
        
        if(meetings.isEmpty() && noReschedulableBreaks.isEmpty())
            return true;
        else {
            for(Meeting meet: meetings)
            {
                meetingsField = meetingsField.concat(meet.getMeetingPK().getMeetingid() + "%");
            }
            
            for(Break b: noReschedulableBreaks){
                breaksField = breaksField.concat(b.getBreakPK().getBreakid() + "%");
           }
            
           w.setMeetings(meetingsField);
           w.setBreaks(breaksField);
          //TODO w.setUsertable();
          //TODO w.setWarningPK();
           
            warningFacade.create(w);
        }
        
        
        
        return false;
    }
 
    //this method computes the overlap between meetings
    public ArrayList<Meeting> CheckMeetingOverlaps(Meeting m) throws IOException, MalformedURLException, ParseException, org.json.simple.parser.ParseException{
        
      List<Meeting> meetings = new ArrayList<>();  
      meetings = (List<Meeting>) meetingFacade.getMeetingsFromUID(m.getMeetingPK().getUid()); //this contains all the meetings of a user
      
      meetings.remove(m); //It depends if it is already inserted in the DB or not
     
      ArrayList<Meeting> conflictuals = new ArrayList<>(); //this will be filled with meetings in conflict with m
      
      int userId= m.getMeetingPK().getUid();
      int mId= m.getMeetingPK().getMeetingid();
      Date mStartDate = m.getStartingdate();
      int mLasts = m.getDuration();
      String mLoc = m.getLocation();
      String mName = m.getName();
     
      for(Meeting other: meetings ){
          
          if(mStartDate == other.getStartingdate())
              conflictuals.add(other);
          else if(mStartDate.getDay() == other.getStartingdate().getDay() && mStartDate.after(other.getStartingdate()) && + other.getDuration()*60 + 
                  other.getStartingdate().getSeconds() + other.getStartingdate().getHours()*3600 + other.getStartingdate().getMinutes()*60 + 
                          nav.retrieveDuration(other.getLocation(), mLoc, String.valueOf(userId))  > mStartDate.getHours()*3600+mStartDate.getMinutes()*60+mStartDate.getSeconds())
              conflictuals.add(other);
          else if (mStartDate.getDay() == other.getStartingdate().getDay() && mStartDate.before(other.getStartingdate()) && 
                  other.getStartingdate().getSeconds() + other.getStartingdate().getHours()*3600 + other.getStartingdate().getMinutes()*60 
                            < mStartDate.getHours()*3600+mStartDate.getMinutes()*60+mStartDate.getSeconds() + nav.retrieveDuration(mLoc, other.getLocation(), String.valueOf(userId)) +
                                    mLasts*60 )
             conflictuals.add(other);
      }

      if(!conflictuals.isEmpty())
          conflictuals.add(m);
      
      return conflictuals;
        
    }
    
    //this method compute the overlaps between m and the breaks
    //it returns an Arraylist filled with break that are in conflict with m
    public ArrayList<Break> BreakConflictChecker(Meeting m){
        
      int userId= m.getMeetingPK().getUid();
      int mId= m.getMeetingPK().getMeetingid();
      Date mStartDate = m.getStartingdate();
      int mLasts = m.getDuration();
      String mLoc = m.getLocation();
      String mName = m.getName();
      
      ArrayList<Break> conflictuals = new ArrayList<Break>();
      
      ArrayList<Break> notReschedulable = new ArrayList<Break>();
      int delta=0; //this value is the overlapping time in seconds of m and a break 
      
      HashMap<Break,Integer> deltas = new HashMap<Break,Integer>();
        
        List<Break> breaks = (List)breakFacade.getBreaksFromUID(userId);
        
        for(Break b: breaks){
      if(b.getDayofweek().subSequence(0, 2).equals(mStartDate.toString().subSequence(0, 2)) && (mStartDate.getHours()*3600 + mStartDate.getMinutes()*60 + mLasts*60 <=
                    b.getEndingtime().getHours()*3600 + b.getEndingtime().getMinutes()*60) && (mStartDate.getHours()*3600 + mStartDate.getMinutes()*60 >= b.getStartingtime().getHours()*3600 + b.getStartingtime().getMinutes()*60)){
                
                conflictuals.add(b);
                   
            }
      else if ((mStartDate.getHours()*3600 + mStartDate.getMinutes()*60 + mLasts*60 >=
                    b.getEndingtime().getHours()*3600 + b.getEndingtime().getMinutes()*60) && (mStartDate.getHours()*3600 + mStartDate.getMinutes()*60 >= b.getStartingtime().getHours()*3600 + b.getStartingtime().getMinutes()*60) &&
              b.getDayofweek().subSequence(0, 2).equals(mStartDate.toString().subSequence(0, 2))){
              
              conflictuals.add(b);
          
      }
      else if(b.getDayofweek().subSequence(0, 2).equals(mStartDate.toString().subSequence(0, 2)) && (b.getStartingtime().getHours()*3600 + b.getStartingtime().getMinutes()*60) >=
                    mStartDate.getHours()*3600 + mStartDate.getMinutes()*60 && (b.getEndingtime().getHours()*3600 + b.getEndingtime().getMinutes()*60) >= mStartDate.getHours()*3600 + mStartDate.getMinutes()*60 + mLasts*60){
             conflictuals.add(b);
             
            }
   
      
      else if(b.getDayofweek().subSequence(0, 2).equals(mStartDate.toString().subSequence(0, 2)) && (b.getStartingtime().getHours()*3600 + b.getStartingtime().getMinutes()*60) <=
                    mStartDate.getHours()*3600 + mStartDate.getMinutes()*60 && (b.getEndingtime().getHours()*3600 + b.getEndingtime().getMinutes()*60) <= mStartDate.getHours()*3600 + mStartDate.getMinutes()*60 + mLasts*60){
             conflictuals.add(b);
             
            }
        }
        return conflictuals;
                      
        
        } 
        
//returns only the breaks without any possible rescheduling
public ArrayList<Break> checkReschedule(int uid){
   
    List<Meeting> meetings = new ArrayList<>();
    HashMap<Meeting,ArrayList<Break>> breaksOfM = new HashMap<>(); //contains all the breaks that are conflictual with m 
   
    meetings = (List<Meeting>) meetingFacade.getMeetingsFromUID(uid);
    
    HashMap<Break,ArrayList<Interval>> intervalsTaken= new HashMap<Break,ArrayList<Interval>>();
    HashMap<Break,Interval> possibleSlot = new HashMap<Break,Interval>();
    ArrayList<Break> result = new ArrayList<Break>();
    
    
    
    
    for(Meeting m : meetings){ // per ogni meeting e ogni break in conflitto con essi calcolo gli intervalli di overlap e li metto in intervalsTaken
        breaksOfM.put(m, this.BreakConflictChecker(m));
        
        
        for(Break b : breaksOfM.get(m)){
            intervalsTaken.keySet().add(b);
            intervalsTaken.get(b).add(this.calculateIntervals(m, b));
        }
    }
    
    for(Break b : intervalsTaken.keySet()){ //per ogni break verifico se c'è un intervallo tra gli intervalli di overlap > della durata del meeting
        
        this.sortIntervals(intervalsTaken.get(b));       //TODO c'è conflitto 
        possibleSlot.put(b, this.checkDistance(intervalsTaken.get(b), b.getMinduration().getHours()*3600 + b.getMinduration().getMinutes()*60));
        
    }
    
    for(Break b: possibleSlot.keySet()){
        if(possibleSlot.get(b).areIntervalsEqual(new Interval(Date.from(Instant.MIN),Date.from(Instant.MIN))))
            result.add(b);
    }
    
    return result;
    
    }


public Interval calculateIntervals(Meeting m, Break b){
    
    Interval delta = new Interval(Date.from(Instant.now()),Date.from(Instant.now()));
    Date flag = new Date();
    
      int userId= m.getMeetingPK().getUid();
      int mId= m.getMeetingPK().getMeetingid();
      Date mStartDate = m.getStartingdate();
      int mLasts = m.getDuration();
      String mLoc = m.getLocation();
      String mName = m.getName();
    
      if(b.getDayofweek().subSequence(0, 2).equals(mStartDate.toString().subSequence(0, 2)) && (mStartDate.getHours()*3600 + mStartDate.getMinutes()*60 + mLasts*60 <=
                    b.getEndingtime().getHours()*3600 + b.getEndingtime().getMinutes()*60) && (mStartDate.getHours()*3600 + mStartDate.getMinutes()*60 >= b.getStartingtime().getHours()*3600 + b.getStartingtime().getMinutes()*60)){
                
                flag = mStartDate;
                flag.setTime(mStartDate.getTime() + mLasts*60*1000);
                delta.setInterval(mStartDate,flag);
                   
            }
      else if ((mStartDate.getHours()*3600 + mStartDate.getMinutes()*60 + mLasts*60 >=
                    b.getEndingtime().getHours()*3600 + b.getEndingtime().getMinutes()*60) && (mStartDate.getHours()*3600 + mStartDate.getMinutes()*60 >= b.getStartingtime().getHours()*3600 + b.getStartingtime().getMinutes()*60) &&
              b.getDayofweek().subSequence(0, 2).equals(mStartDate.toString().subSequence(0, 2))){
              
              delta.setInterval(mStartDate, b.getEndingtime());
          
      }
      else if(b.getDayofweek().subSequence(0, 2).equals(mStartDate.toString().subSequence(0, 2)) && (b.getStartingtime().getHours()*3600 + b.getStartingtime().getMinutes()*60) >=
                    mStartDate.getHours()*3600 + mStartDate.getMinutes()*60 && (b.getEndingtime().getHours()*3600 + b.getEndingtime().getMinutes()*60) >= mStartDate.getHours()*3600 + mStartDate.getMinutes()*60 + mLasts*60){
             flag = mStartDate;
             flag.setTime(mStartDate.getTime() + mLasts*60*1000);
             delta.setInterval( b.getStartingtime(), flag);
            }
      else if(b.getDayofweek().subSequence(0, 2).equals(mStartDate.toString().subSequence(0, 2)) && (b.getStartingtime().getHours()*3600 + b.getStartingtime().getMinutes()*60) <=
                    mStartDate.getHours()*3600 + mStartDate.getMinutes()*60 && (b.getEndingtime().getHours()*3600 + b.getEndingtime().getMinutes()*60) <= mStartDate.getHours()*3600 + mStartDate.getMinutes()*60 + mLasts*60){
             delta.setInterval(b.getStartingtime(), b.getEndingtime());
             
            }
    
    return delta;
}

private class Interval{
    Date start;
    Date end;
    
    Interval(Date start, Date end){
        this.start = start;
        this.end = end;
    }
    
    public void setInterval(Date start, Date end){
        this.start = start;
        this.end = end;
    }
    
     public boolean areIntervalsEqual(Interval i1){
       if(i1.start.equals(this.start) && i1.end.equals(this.end))
           return true;
       
       return false;
       
   }
    
        
    }
    
//this is a method to sort (form the earliest to the latest) an arraylist of intervals
   public void sortIntervals(ArrayList<Interval> i){
    
    Interval flag = new Interval(Date.from(Instant.now()),Date.from(Instant.now()));
    for(int j=0; j<i.size()-1;j++){
        if(i.get(j).start.before(i.get(j+1).start) && i.get(j).end.before(i.get(j+1).start)) // se in precede flag 
        {
            
        }else {
            flag = i.get(j);
            i.set(j, i.get(j+1));
            i.set(j+1, flag);
        }
        
    }
    
    
}
   
   //this is a method that given an arraylist of Interval, checks whether there is an Interval of @param dist between two intervals in the arraylist and returns it
   public Interval checkDistance(ArrayList<Interval> i, int dist){
       
       for(int j=0; j<i.size()-1;j++){
           if(i.get(j+1).start.getTime()-i.get(j).end.getTime() > dist*1000){
               return new Interval(i.get(j).end,i.get(j+1).start);
           }
           
       }
       return new Interval(Date.from(Instant.MIN),Date.from(Instant.MIN));
      
   }
    
  
}
    


