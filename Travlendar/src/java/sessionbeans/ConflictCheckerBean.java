/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionbeans;

import entities.Break;
import entities.Meeting;
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
import javax.ejb.EJB;
import javax.ejb.Stateless;

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
    
      int userId= m.getMeetingPK().getUid();
      int mId= m.getMeetingPK().getMeetingid();
      Date mStartDate = m.getStartingdate();
      int mLasts = m.getDuration();
      String mLoc = m.getLocation();
      String mName = m.getName();
    
    //this method computes the overlap between meetings
    public ArrayList<Meeting> CheckMeetingOverlaps(Meeting m) throws IOException, MalformedURLException, ParseException, org.json.simple.parser.ParseException{
        
      ArrayList<Meeting> meetings = new ArrayList<Meeting>();  
      meetings = (ArrayList<Meeting>) meetingFacade.getMeetingsFromUID(m.getMeetingPK().getUid()); //this contains all the meetings of a user
      
      meetings.remove(m); //It depends if it is already inserted in the DB or not
     
      ArrayList<Meeting> conflictuals = new ArrayList<Meeting>(); //this will be filled with meetings in conflict with m
      
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
    //it returns the breakId of the conflictual break, in case of no conflict it returns -1
    public ArrayList<Break> BreakConflictChecker(Meeting m){
        
      int userId= m.getMeetingPK().getUid();
      int mId= m.getMeetingPK().getMeetingid();
      Date mStartDate = m.getStartingdate();
      int mLasts = m.getDuration();
      String mLoc = m.getLocation();
      String mName = m.getName();
      
      ArrayList<Break> conflictuals = new ArrayList<Break>();
      int delta=0; //this value is the overlapping time in seconds of m and a break 
      
      HashMap<Break,Integer> deltas = new HashMap<Break,Integer>();
        
        ArrayList<Break> breaks = (ArrayList)breakFacade.getBreaksFromUID(userId);
        
        for(Break b: breaks){
      if(b.getDayofweek().subSequence(0, 2).equals(mStartDate.toString().subSequence(0, 2)) && (mStartDate.getHours()*3600 + mStartDate.getMinutes()*60 + mLasts*60 <
                    b.getEndingtime().getHours()*3600 + b.getEndingtime().getMinutes()*60) && (mStartDate.getHours()*3600 + mStartDate.getMinutes()*60 > b.getStartingtime().getHours()*3600 + b.getStartingtime().getMinutes()*60)){
                
                conflictuals.add(b);
                   
            }
      else if ((mStartDate.getHours()*3600 + mStartDate.getMinutes()*60 + mLasts*60 >
                    b.getEndingtime().getHours()*3600 + b.getEndingtime().getMinutes()*60) && (mStartDate.getHours()*3600 + mStartDate.getMinutes()*60 > b.getStartingtime().getHours()*3600 + b.getStartingtime().getMinutes()*60) &&
              b.getDayofweek().subSequence(0, 2).equals(mStartDate.toString().subSequence(0, 2))){
              
              conflictuals.add(b);
          
      }
      else if(b.getDayofweek().subSequence(0, 2).equals(mStartDate.toString().subSequence(0, 2)) && (b.getStartingtime().getHours()*3600 + b.getStartingtime().getMinutes()*60) >
                    mStartDate.getHours()*3600 + mStartDate.getMinutes()*60 && (b.getEndingtime().getHours()*3600 + b.getEndingtime().getMinutes()*60) > mStartDate.getHours()*3600 + mStartDate.getMinutes()*60 + mLasts*60){
             conflictuals.add(b);
             
            }
   
        }
                        
         return conflictuals;               
        
        }
        

public void checkReschedule(String uid){
   
    ArrayList<Meeting> meetings = new ArrayList<Meeting>();
    HashMap<Meeting,ArrayList<Break>> breaksOfM = new HashMap<Meeting,ArrayList<Break>>(); //contains all the breaks that are conflictual with m 
   
    meetings = (ArrayList<Meeting>) meetingFacade.getMeetingsFromUID(Integer.parseInt(uid));
    
    HashMap<Break,ArrayList<Interval>> intervalsTaken= new HashMap<Break,ArrayList<Interval>>();
    
    ArrayList<Interval> flag = new ArrayList<Interval>();
    
    for(Meeting m : meetings){
        breaksOfM.put(m, this.BreakConflictChecker(m));
        for(Break b : breaksOfM.get(m)){
            intervalsTaken.get(b).add(this.calculateIntervals(m, b));
        }
    }
    
    for(Break b : intervalsTaken.keySet()){
        if(b.getMinduration().getHours()*3600 + b.getMinduration().getMinutes()*60 > b.getEndingtime().getHours()*3600 + b.getEndingtime().getMinutes()*60
                - b.getStartingtime().getHours()*3600 - b.getStartingtime().getMinutes()*60 - intervalsTaken.get(b))
            //TODO c'è conflitto 
            
    }
    
    
    }


public Interval calculateIntervals(Meeting m, Break b){
    
    Interval delta = new Interval(Date.from(Instant.now()),Date.from(Instant.now()));
    Date flag = new Date();
    
      if(b.getDayofweek().subSequence(0, 2).equals(mStartDate.toString().subSequence(0, 2)) && (mStartDate.getHours()*3600 + mStartDate.getMinutes()*60 + mLasts*60 <
                    b.getEndingtime().getHours()*3600 + b.getEndingtime().getMinutes()*60) && (mStartDate.getHours()*3600 + mStartDate.getMinutes()*60 > b.getStartingtime().getHours()*3600 + b.getStartingtime().getMinutes()*60)){
                
                flag = mStartDate;
                flag.setTime(mStartDate.getTime() + mLasts*60*1000);
                delta.setInterval(mStartDate,flag);
                   
            }
      else if ((mStartDate.getHours()*3600 + mStartDate.getMinutes()*60 + mLasts*60 >
                    b.getEndingtime().getHours()*3600 + b.getEndingtime().getMinutes()*60) && (mStartDate.getHours()*3600 + mStartDate.getMinutes()*60 > b.getStartingtime().getHours()*3600 + b.getStartingtime().getMinutes()*60) &&
              b.getDayofweek().subSequence(0, 2).equals(mStartDate.toString().subSequence(0, 2))){
              
              delta.setInterval(mStartDate, b.getEndingtime());
          
      }
      else if(b.getDayofweek().subSequence(0, 2).equals(mStartDate.toString().subSequence(0, 2)) && (b.getStartingtime().getHours()*3600 + b.getStartingtime().getMinutes()*60) >
                    mStartDate.getHours()*3600 + mStartDate.getMinutes()*60 && (b.getEndingtime().getHours()*3600 + b.getEndingtime().getMinutes()*60) > mStartDate.getHours()*3600 + mStartDate.getMinutes()*60 + mLasts*60){
             flag = mStartDate;
             flag.setTime(mStartDate.getTime() + mLasts*60*1000);
             delta.setInterval( b.getStartingtime(), flag);
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
    
    public void appendInterval(Interval i1){
        
        if(i1.start.before(this.start) && i1.end.before(this.start) || (i1.start.before(this.start) && i1.end.after(this.start))){
            this.start = i1.start;
            this.end = this.end;
        }
        
    }
    
    
}
    
   
}
