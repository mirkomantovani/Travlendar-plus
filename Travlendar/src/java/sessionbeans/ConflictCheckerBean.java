/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionbeans;

import entities.Break;
import entities.Meeting;
import entities.Warning;
import entities.WarningPK;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    
    @EJB
    private WarningFacadeLocal warningFacade;
    
    //returns true if m is feasible, false otherwise
    public boolean CheckAllConflicts(Meeting m){
        
        ArrayList<Break> rescheduleChecked = new ArrayList<Break>();
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
        rescheduleChecked = checkReschedule(m.getMeetingPK().getUid());
        
        List<Break> noReschedulableBreaks = new ArrayList<Break>();
        
        for(Break b: rescheduleChecked){
            if(this.BreakConflictChecker(m).contains(b)){
                noReschedulableBreaks.add(b);
            }
        }
        
        String meetingsField="";
        String breaksField="";
        
        System.out.print("stampo no reschedulable breaks:" + noReschedulableBreaks.toString());
        System.out.print("stampo meetings:" + meetings.toString());
        
        if(meetings.isEmpty() && noReschedulableBreaks.isEmpty())
            return true;
        else {
            if(!meetings.isEmpty()){
                meetings.remove(m);

            for(Meeting meet: meetings)
            {
                meetingsField = meetingsField.concat(meet.getMeetingPK().getMeetingid() + "%");
            }
            }
            
            if(!noReschedulableBreaks.isEmpty()){
             
                for(Break b: noReschedulableBreaks){
                    breaksField = breaksField.concat(b.getBreakPK().getBreakid() + "%");
                }
            }
            
            String existence = this.checkWarningExistence(meetingsField, breaksField, String.valueOf(m.getMeetingPK().getUid()));
           
           if(existence.equals("not exists")){
              w.setMeetings(meetingsField.concat(String.valueOf(m.getMeetingPK().getMeetingid()).concat("%")));
              w.setBreaks(breaksField);
              WarningPK wPK = new WarningPK();
              wPK.setUid(m.getMeetingPK().getUid());
              String wID = w.getMeetings()+ String.valueOf(m.getMeetingPK().getUid());
              wPK.setWarningid(wID.hashCode());
              w.setWarningPK(wPK);
              w.setUsertable(m.getUsertable());
              warningFacade.create(w); 
           }
           else{
              
               Warning existent = new Warning();
               
               WarningPK wPK = new WarningPK();
               
               wPK.setUid(m.getMeetingPK().getUid());
               wPK.setWarningid(Integer.parseInt(existence));
              
               existent = warningFacade.find(wPK);
               
               existent.setMeetings(existent.getMeetings().concat(String.valueOf(m.getMeetingPK().getMeetingid()).concat("%")));
              
               warningFacade.edit(existent);
           }
            
           this.cleanWarnings(String.valueOf(m.getMeetingPK().getUid())s;
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
          else if(mStartDate.getDay() == other.getStartingdate().getDay() && mStartDate.after(other.getStartingdate()) &&  other.getDuration()*60 + 
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
      String dataM ="";
      
      ArrayList<Break> conflictuals = new ArrayList<Break>();
      
      ArrayList<Break> notReschedulable = new ArrayList<Break>();
      int delta=0; //this value is the overlapping time in seconds of m and a break 
      
      HashMap<Break,Integer> deltas = new HashMap<Break,Integer>();
        
        List<Break> breaks = (List<Break>)breakFacade.getBreaksFromUID(userId);
        
        System.out.println("I break nel db sono:" + breaks.toString());
       
        dataM = this.returnDayOfWeek(mStartDate);
    
        for(Break b: breaks){
            System.out.println("Il break inizia" + (b.getStartingtime().getHours()*3600 + b.getStartingtime().getMinutes()*60) + "e finisce" + (b.getEndingtime().getHours()*3600 + b.getEndingtime().getMinutes()*60));
            System.out.println("il meet inizia:" + (mStartDate.getHours()*3600 + mStartDate.getMinutes()*60) + "e finisce:" + (mStartDate.getHours()*3600 + mStartDate.getMinutes()*60 + mLasts*60));
            System.out.println("il break è il giorno: " + b.getDayofweek().subSequence(0, 2));
            System.out.println("il meet è il giorno: " + mStartDate.toString().toLowerCase().subSequence(0, 2));
            System.out.println(mStartDate.toString());
      if(b.getDayofweek().subSequence(0, 2).equals(dataM.subSequence(0, 2)) && (mStartDate.getHours()*3600 + mStartDate.getMinutes()*60 + mLasts*60 <=
                    b.getEndingtime().getHours()*3600 + b.getEndingtime().getMinutes()*60) && (mStartDate.getHours()*3600 + mStartDate.getMinutes()*60 >= b.getStartingtime().getHours()*3600 + b.getStartingtime().getMinutes()*60)){
                // meeting finisce prima che finisca il break, meeting inizia dopo che inizia il break (meeting incluso nella break)
                conflictuals.add(b);
                System.out.print("sono nel primo if");
                   
            }
      else if ((mStartDate.getHours()*3600 + mStartDate.getMinutes()*60 + mLasts*60 >=
                    b.getEndingtime().getHours()*3600 + b.getEndingtime().getMinutes()*60) && (mStartDate.getHours()*3600 + mStartDate.getMinutes()*60 >= b.getStartingtime().getHours()*3600 + b.getStartingtime().getMinutes()*60) &&
              b.getDayofweek().subSequence(0, 2).equals(dataM.subSequence(0, 2))){
              //meeting inizia dopo il break, meeting finisce dopo break 
              conflictuals.add(b);
          System.out.print("sono nel secondo else if");
      }
      else if(b.getDayofweek().subSequence(0, 2).equals(dataM.subSequence(0, 2)) && (b.getStartingtime().getHours()*3600 + b.getStartingtime().getMinutes()*60) >=
                    mStartDate.getHours()*3600 + mStartDate.getMinutes()*60 && (b.getEndingtime().getHours()*3600 + b.getEndingtime().getMinutes()*60) >= mStartDate.getHours()*3600 + mStartDate.getMinutes()*60 + mLasts*60){
             conflictuals.add(b);
             System.out.print("sono nel terzo else if");
             //break inizia dopo il meeting, break finisce dopo il meeting 
            }
   
      
      else if(b.getDayofweek().subSequence(0, 2).equals(dataM.subSequence(0, 2)) && (b.getStartingtime().getHours()*3600 + b.getStartingtime().getMinutes()*60) >=
                    mStartDate.getHours()*3600 + mStartDate.getMinutes()*60 && (b.getEndingtime().getHours()*3600 + b.getEndingtime().getMinutes()*60) <= mStartDate.getHours()*3600 + mStartDate.getMinutes()*60 + mLasts*60){
             conflictuals.add(b);
             System.out.print("sono nel quarto else if");
             //break interno al meeting
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
    HashMap<Break,Boolean> possibleSlot = new HashMap<Break,Boolean>();
    ArrayList<Break> result = new ArrayList<Break>();
    
    
    System.out.println("");
    
    for(Meeting m : meetings){ // per ogni meeting e ogni break in conflitto con essi calcolo gli intervalli di overlap e li metto in intervalsTaken
        breaksOfM.put(m, this.BreakConflictChecker(m));
        
        System.out.println("breaksOfM:" + breaksOfM.toString());
        
        for(Break b : breaksOfM.get(m)){
            intervalsTaken.put(b, new ArrayList<Interval>());
            intervalsTaken.get(b).add(this.calculateIntervals(m, b));
           
            
        }
    }
    
    System.out.println("intervalsTaken:" + intervalsTaken.toString());
    
    for(Break b : intervalsTaken.keySet()){ //per ogni break verifico se c'è un intervallo tra gli intervalli di overlap > della durata del meeting
        
        this.sortIntervals(intervalsTaken.get(b));       //TODO c'è conflitto 
        possibleSlot.put(b, this.checkDistance(intervalsTaken.get(b), b.getMinduration().getHours()*3600 + b.getMinduration().getMinutes()*60));
        
    }
    
    System.out.println("possibleSlot:" + possibleSlot.toString());
    
    for(Break b: possibleSlot.keySet()){
        if(!possibleSlot.get(b))
            result.add(b);
    }
    
    System.out.println("result:" + result.toString());
    
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
   public boolean checkDistance(ArrayList<Interval> i, int dist){
       
       for(int j=0; j<i.size()-1;j++){
           if(i.get(j+1).start.getTime()-i.get(j).end.getTime() > dist*1000){
               System.out.println("dist:" + dist*1000+ "cond:" + (i.get(j+1).start.getTime()-i.get(j).end.getTime()));
               return true;
               
                     
           }
           
       }
       return false;
      
   }
   
   public String checkWarningExistence(String meetings,String breaks,String uid){
       
       List<Warning> warnings = new ArrayList<Warning>();
       warnings = warningFacade.getWarningsFromUID(Integer.parseInt(uid));
       
       String[] Ms;
       Ms = meetings.split("%");
       String[] Bs;
       Bs = breaks.split("%");
       
       
       
       for(Warning w: warnings){
           int mCounter = 0;
           int bCounter = 0;
           for(int i=0; i<Ms.length;i++){
              if(w.getMeetings().contains(Ms[i])){
                mCounter++; 
              }
          }
           for(int j=0; j<Bs.length;j++){
               if(w.getBreaks().contains(Bs[j])){
                   bCounter++;
               }
           }
          
           if(mCounter == Ms.length && bCounter == Bs.length ){
               return String.valueOf(w.getWarningPK().getWarningid());
           }
           
       }
       
       return "not exists";
       
   }
    
   public String returnDayOfWeek(Date d){
      
       switch(d.getDay()){
           case 0: return "sunday";
           case 1: return "monday";
           case 2: return "tuesday";
           case 3: return "wednesday";
           case 4: return "thursday";
           case 5: return "friday";
           case 6: return "saturday";
        
       }
       return "";
   }
   
   // it removes warnings which are included in other warning 
   public void cleanWarnings(String uid){
       List<Warning> warnings = warningFacade.getWarningsFromUID(Integer.parseInt(uid));
       
       for(Warning w: warnings){
           
           for(Warning w1 : warnings){
               if(w != w1 && w.getMeetings().contains(w1.getMeetings()) && w.getBreaks().contains(w1.getBreaks())){
                   warningFacade.remove(w1);
               }
           }
          
       }
   }
}
    


