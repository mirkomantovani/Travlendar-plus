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
            
           this.cleanWarnings(String.valueOf(m.getMeetingPK().getUid()));
        }
        
        
        
        return false;
    }
 
    //this method computes the overlap between meetings
    public ArrayList<Meeting> CheckMeetingOverlaps(Meeting m) throws IOException, MalformedURLException, ParseException, org.json.simple.parser.ParseException{
        
      List<Meeting> meetings = new ArrayList<>();  
      meetings = (List<Meeting>) meetingFacade.getMeetingsFromUID(m.getMeetingPK().getUid()); //this contains all the meetings of a user
      
      meetings.remove(m); //it will be added when the conflictual meetings are computed 
     
      ArrayList<Meeting> conflictuals = new ArrayList<>(); //this will be filled with meetings in conflict with m
      
      int userId= m.getMeetingPK().getUid();
      int mId= m.getMeetingPK().getMeetingid();
      Date mStartDate = m.getStartingdate();
      int mLasts = m.getDuration();
      String mLoc = m.getLocation();
      String mName = m.getName();
     
      for(Meeting other: meetings ){
          
          if(mStartDate == other.getStartingdate()) // if two meetings are in the same date are in conflict
              conflictuals.add(other);
          else if(mStartDate.getDay() == other.getStartingdate().getDay() && mStartDate.getDate() == other.getStartingdate().getDate() && mStartDate.getMonth()==other.getStartingdate().getMonth()
                  && mStartDate.getYear() == other.getStartingdate().getYear() && mStartDate.after(other.getStartingdate()) &&  other.getDuration()*60 + 
                  other.getStartingdate().getSeconds() + other.getStartingdate().getHours()*3600 + other.getStartingdate().getMinutes()*60 + 
                          nav.retrieveDuration(other.getLocation(), mLoc, String.valueOf(userId))  > mStartDate.getHours()*3600+mStartDate.getMinutes()*60+mStartDate.getSeconds())
              conflictuals.add(other);
          else if (mStartDate.getDay() == other.getStartingdate().getDay()  && mStartDate.getDate() == other.getStartingdate().getDate() && mStartDate.getMonth()==other.getStartingdate().getMonth()
                  && mStartDate.getYear() == other.getStartingdate().getYear() && mStartDate.before(other.getStartingdate()) && 
                  other.getStartingdate().getSeconds() + other.getStartingdate().getHours()*3600 + other.getStartingdate().getMinutes()*60 
                            < mStartDate.getHours()*3600+mStartDate.getMinutes()*60+mStartDate.getSeconds() + nav.retrieveDuration(mLoc, other.getLocation(), String.valueOf(userId)) +
                                    mLasts*60 )
             conflictuals.add(other);
      }

      if(!conflictuals.isEmpty())
          conflictuals.add(m);
      
      return conflictuals;
        
    }
    
    //this method compute the overlaps between m and the user breaks
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
        
        List<Break> breaks = (List<Break>)breakFacade.getBreaksFromUID(userId); //list filled with all the breaks in the db
        
        System.out.println("I break nel db sono:" + breaks.toString());
       
        dataM = this.returnDayOfWeek(mStartDate);
    
        for(Break b: breaks){
        if(b.getRecurrent()){
     
            System.out.println(mStartDate.toString());
      if(b.getDayofweek().subSequence(0, 2).equals(dataM.subSequence(0, 2)) && (mStartDate.getHours()*3600 + mStartDate.getMinutes()*60 + mLasts*60 <=
                    b.getEndingtime().getHours()*3600 + b.getEndingtime().getMinutes()*60) && (mStartDate.getHours()*3600 + mStartDate.getMinutes()*60 >= b.getStartingtime().getHours()*3600 + b.getStartingtime().getMinutes()*60)){
                // meeting ends before the break ends and starts after the break begins (meeting included in the break)
                conflictuals.add(b);
                
                   
            }
      else if ((mStartDate.getHours()*3600 + mStartDate.getMinutes()*60 + mLasts*60 >=
                    b.getEndingtime().getHours()*3600 + b.getEndingtime().getMinutes()*60) && (mStartDate.getHours()*3600 + mStartDate.getMinutes()*60 >= b.getStartingtime().getHours()*3600 + b.getStartingtime().getMinutes()*60) &&
              b.getDayofweek().subSequence(0, 2).equals(dataM.subSequence(0, 2))){
              //meeting starts after the break, meeting ends after the break 
              conflictuals.add(b);
       
      }
      else if(b.getDayofweek().subSequence(0, 2).equals(dataM.subSequence(0, 2)) && (b.getStartingtime().getHours()*3600 + b.getStartingtime().getMinutes()*60) >=
                    mStartDate.getHours()*3600 + mStartDate.getMinutes()*60 && (b.getEndingtime().getHours()*3600 + b.getEndingtime().getMinutes()*60) >= mStartDate.getHours()*3600 + mStartDate.getMinutes()*60 + mLasts*60){
             conflictuals.add(b);
             
             //break starts after the meeting, break ends after the meeting 
            }
   
      
      else if(b.getDayofweek().subSequence(0, 2).equals(dataM.subSequence(0, 2)) && (b.getStartingtime().getHours()*3600 + b.getStartingtime().getMinutes()*60) >=
                    mStartDate.getHours()*3600 + mStartDate.getMinutes()*60 && (b.getEndingtime().getHours()*3600 + b.getEndingtime().getMinutes()*60) <= mStartDate.getHours()*3600 + mStartDate.getMinutes()*60 + mLasts*60){
             conflictuals.add(b);
             
             //break included in the meeting
            }
        }
      
        else { //same code written above but the day,month and year of the break should be now considered because this branch regards not recurrent breaks
                if(b.getDayofweek().subSequence(0, 2).equals(dataM.subSequence(0, 2)) &&mStartDate.getDate() == b.getStartingtime().getDate() && mStartDate.getMonth() == b.getStartingtime().getMonth()
                && mStartDate.getYear() == b.getStartingtime().getYear() && (mStartDate.getHours()*3600 + mStartDate.getMinutes()*60 + mLasts*60 <=
                    b.getEndingtime().getHours()*3600 + b.getEndingtime().getMinutes()*60) && (mStartDate.getHours()*3600 + mStartDate.getMinutes()*60 >= b.getStartingtime().getHours()*3600 + b.getStartingtime().getMinutes()*60)){
                
                conflictuals.add(b);
                   
            }
      else if ((mStartDate.getHours()*3600 + mStartDate.getMinutes()*60 + mLasts*60 >=
                    b.getEndingtime().getHours()*3600 + b.getEndingtime().getMinutes()*60) &&mStartDate.getDate() == b.getStartingtime().getDate() && mStartDate.getMonth() == b.getStartingtime().getMonth()
                && mStartDate.getYear() == b.getStartingtime().getYear() && (mStartDate.getHours()*3600 + mStartDate.getMinutes()*60 >= b.getStartingtime().getHours()*3600 + b.getStartingtime().getMinutes()*60) &&
              b.getDayofweek().subSequence(0, 2).equals(dataM.subSequence(0, 2))){
              
              conflictuals.add(b);
          
      }
      else if(b.getDayofweek().subSequence(0, 2).equals(dataM.subSequence(0, 2))  &&mStartDate.getDate() == b.getStartingtime().getDate() && mStartDate.getMonth() == b.getStartingtime().getMonth()
                && mStartDate.getYear() == b.getStartingtime().getYear() && (b.getStartingtime().getHours()*3600 + b.getStartingtime().getMinutes()*60) >=
                    mStartDate.getHours()*3600 + mStartDate.getMinutes()*60 && (b.getEndingtime().getHours()*3600 + b.getEndingtime().getMinutes()*60) >= mStartDate.getHours()*3600 + mStartDate.getMinutes()*60 + mLasts*60){
             conflictuals.add(b);
            }
      else if(b.getDayofweek().subSequence(0, 2).equals(dataM.subSequence(0, 2))  &&mStartDate.getDate() == b.getStartingtime().getDate() && mStartDate.getMonth() == b.getStartingtime().getMonth()
                && mStartDate.getYear() == b.getStartingtime().getYear() && (b.getStartingtime().getHours()*3600 + b.getStartingtime().getMinutes()*60) <=
                    mStartDate.getHours()*3600 + mStartDate.getMinutes()*60 && (b.getEndingtime().getHours()*3600 + b.getEndingtime().getMinutes()*60) <= mStartDate.getHours()*3600 + mStartDate.getMinutes()*60 + mLasts*60){
             
             conflictuals.add(b);
            }
                }
        
        }
        return conflictuals;
        } 
        
//returns only the breaks without any possible rescheduling
public ArrayList<Break> checkReschedule(int uid){
   
    List<Meeting> meetings = new ArrayList<>();
    HashMap<Meeting,ArrayList<Break>> breaksOfM = new HashMap<>(); //contains all the breaks that are conflictual with m 
   
    meetings = (List<Meeting>) meetingFacade.getMeetingsFromUID(uid);
    
    HashMap<Break,ArrayList<Interval>> intervalsTaken= new HashMap<Break,ArrayList<Interval>>(); //For every break i compute all the intervals which are taken between its starting and ending time
    HashMap<Break,Boolean> possibleSlot = new HashMap<Break,Boolean>(); //Every break is related to a boolean value that explains if it is reschedulable or not 
    ArrayList<Break> result = new ArrayList<Break>();
    
    
    System.out.println("");
    
    for(Meeting m : meetings){ // for every meeting and every break that is in conflict with them i compute the overlapping and i store them in intervalsTaken
        breaksOfM.put(m, this.BreakConflictChecker(m));
        
        System.out.println("breaksOfM:" + breaksOfM.toString());
        
        for(Break b : breaksOfM.get(m)){
            intervalsTaken.put(b, new ArrayList<Interval>());
            intervalsTaken.get(b).add(this.calculateIntervals(m, b));
           
            
        }
    }
    
    System.out.println("intervalsTaken:" + intervalsTaken.toString());
    
    for(Break b : intervalsTaken.keySet()){ //for every break i evaluate if there is an interval among the taken intervals whihch is grater or equal to the break duration(= which allows the flexible reschedulation)
       
        
        this.sortIntervals(intervalsTaken.get(b)); // i sort in cronological order the intervals taken       
        possibleSlot.put(b, this.checkDistance(intervalsTaken.get(b), b.getMinduration().getHours()*3600 + b.getMinduration().getMinutes()*60));
        
    }
    
    System.out.println("possibleSlot:" + possibleSlot.toString());
    
    for(Break b: possibleSlot.keySet()){
        if(!possibleSlot.get(b)) // if possibleSlot.get(b) is true it means that the flexible reschedulation can occur, so i fill an arrayList result with the breaks related to a false value 
            result.add(b);
    }
    
    System.out.println("result:" + result.toString());
    
    return result;
    
    }

//this method checks if during the break duration there is time to have the break, considering all the conflict in which it is involved and returns the first possible slot
//if absent it returns the current instant. 
public Interval calculateIntervals(Meeting m, Break b){
    
    Interval delta = new Interval(Date.from(Instant.now()),Date.from(Instant.now()));
    Date flag = new Date();
    
      int userId= m.getMeetingPK().getUid();
      int mId= m.getMeetingPK().getMeetingid();
      Date mStartDate = m.getStartingdate();
      int mLasts = m.getDuration();
      String mLoc = m.getLocation();
      String mName = m.getName();
      
      String dataM="";
      
      dataM = this.returnDayOfWeek(mStartDate);
   
    if(b.getRecurrent()){
    
      if(b.getDayofweek().subSequence(0, 2).equals(dataM.subSequence(0, 2)) && (mStartDate.getHours()*3600 + mStartDate.getMinutes()*60 + mLasts*60 <=
                    b.getEndingtime().getHours()*3600 + b.getEndingtime().getMinutes()*60) && (mStartDate.getHours()*3600 + mStartDate.getMinutes()*60 >= b.getStartingtime().getHours()*3600 + b.getStartingtime().getMinutes()*60)){
                
                flag = mStartDate;
                flag.setTime(mStartDate.getTime() + mLasts*60*1000);
                delta.setInterval(mStartDate,flag);
                   
            }
      else if ((mStartDate.getHours()*3600 + mStartDate.getMinutes()*60 + mLasts*60 >=
                    b.getEndingtime().getHours()*3600 + b.getEndingtime().getMinutes()*60) && (mStartDate.getHours()*3600 + mStartDate.getMinutes()*60 >= b.getStartingtime().getHours()*3600 + b.getStartingtime().getMinutes()*60) &&
              b.getDayofweek().subSequence(0, 2).equals(dataM.subSequence(0, 2))){
              
              delta.setInterval(mStartDate, b.getEndingtime());
          
      }
      else if(b.getDayofweek().subSequence(0, 2).equals(dataM.subSequence(0, 2)) && (b.getStartingtime().getHours()*3600 + b.getStartingtime().getMinutes()*60) >=
                    mStartDate.getHours()*3600 + mStartDate.getMinutes()*60 && (b.getEndingtime().getHours()*3600 + b.getEndingtime().getMinutes()*60) >= mStartDate.getHours()*3600 + mStartDate.getMinutes()*60 + mLasts*60){
             flag = mStartDate;
             flag.setTime(mStartDate.getTime() + mLasts*60*1000);
             delta.setInterval( b.getStartingtime(), flag);
            }
      else if(b.getDayofweek().subSequence(0, 2).equals(dataM.subSequence(0, 2)) && (b.getStartingtime().getHours()*3600 + b.getStartingtime().getMinutes()*60) <=
                    mStartDate.getHours()*3600 + mStartDate.getMinutes()*60 && (b.getEndingtime().getHours()*3600 + b.getEndingtime().getMinutes()*60) <= mStartDate.getHours()*3600 + mStartDate.getMinutes()*60 + mLasts*60){
             delta.setInterval(b.getStartingtime(), b.getEndingtime());
             
            }
    }else{ //same code written above but the day,month and year of the break should be now considered because this branch regards not recurrent breaks
        if(b.getDayofweek().subSequence(0, 2).equals(dataM.subSequence(0, 2)) &&mStartDate.getDate() == b.getStartingtime().getDate() && mStartDate.getMonth() == b.getStartingtime().getMonth()
                && mStartDate.getYear() == b.getStartingtime().getYear() && (mStartDate.getHours()*3600 + mStartDate.getMinutes()*60 + mLasts*60 <=
                    b.getEndingtime().getHours()*3600 + b.getEndingtime().getMinutes()*60) && (mStartDate.getHours()*3600 + mStartDate.getMinutes()*60 >= b.getStartingtime().getHours()*3600 + b.getStartingtime().getMinutes()*60)){
                
                flag = mStartDate;
                flag.setTime(mStartDate.getTime() + mLasts*60*1000);
                delta.setInterval(mStartDate,flag);
                   
            }
      else if ((mStartDate.getHours()*3600 + mStartDate.getMinutes()*60 + mLasts*60 >=
                    b.getEndingtime().getHours()*3600 + b.getEndingtime().getMinutes()*60) &&mStartDate.getDate() == b.getStartingtime().getDate() && mStartDate.getMonth() == b.getStartingtime().getMonth()
                && mStartDate.getYear() == b.getStartingtime().getYear() && (mStartDate.getHours()*3600 + mStartDate.getMinutes()*60 >= b.getStartingtime().getHours()*3600 + b.getStartingtime().getMinutes()*60) &&
              b.getDayofweek().subSequence(0, 2).equals(dataM.subSequence(0, 2))){
              
              delta.setInterval(mStartDate, b.getEndingtime());
          
      }
      else if(b.getDayofweek().subSequence(0, 2).equals(dataM.subSequence(0, 2))  &&mStartDate.getDate() == b.getStartingtime().getDate() && mStartDate.getMonth() == b.getStartingtime().getMonth()
                && mStartDate.getYear() == b.getStartingtime().getYear() && (b.getStartingtime().getHours()*3600 + b.getStartingtime().getMinutes()*60) >=
                    mStartDate.getHours()*3600 + mStartDate.getMinutes()*60 && (b.getEndingtime().getHours()*3600 + b.getEndingtime().getMinutes()*60) >= mStartDate.getHours()*3600 + mStartDate.getMinutes()*60 + mLasts*60){
             flag = mStartDate;
             flag.setTime(mStartDate.getTime() + mLasts*60*1000);
             delta.setInterval( b.getStartingtime(), flag);
            }
      else if(b.getDayofweek().subSequence(0, 2).equals(dataM.subSequence(0, 2))  &&mStartDate.getDate() == b.getStartingtime().getDate() && mStartDate.getMonth() == b.getStartingtime().getMonth()
                && mStartDate.getYear() == b.getStartingtime().getYear() && (b.getStartingtime().getHours()*3600 + b.getStartingtime().getMinutes()*60) <=
                    mStartDate.getHours()*3600 + mStartDate.getMinutes()*60 && (b.getEndingtime().getHours()*3600 + b.getEndingtime().getMinutes()*60) <= mStartDate.getHours()*3600 + mStartDate.getMinutes()*60 + mLasts*60){
             delta.setInterval(b.getStartingtime(), b.getEndingtime());
             
            }
    }
    
    return delta;
}

//This private class represent the interval concept, it is necessary to estimate the faculty to flexible reschedule a break 
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
   
   //this is a method that given an arraylist of Interval, checks whether there is an Interval of @param dist distance between two intervals in the arraylist and returns true if it finds it, false otherwise
   public boolean checkDistance(ArrayList<Interval> i, int dist){
       
       for(int j=0; j<i.size()-1;j++){
           if(i.get(j+1).start.getTime()-i.get(j).end.getTime() > dist*1000){
               System.out.println("dist:" + dist*1000+ "cond:" + (i.get(j+1).start.getTime()-i.get(j).end.getTime()));
               return true;
               
                     
           }
           
       }
       return false;
      
   }
   
   //This method is used to check if a warning is already existent and should be edit and retrurns its warningid, or if it is necessary to create a new one and returns "not exists"
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
          
           if(mCounter == Ms.length && bCounter == Bs.length && mCounter != 0 && bCounter != 0){
               return String.valueOf(w.getWarningPK().getWarningid());
           }
           
       }
       
       return "not exists";
       
   }
    
   // This method receives as a parameter an integer related to a day of week and returns the string which represents textually that day
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
   
   // it removes warnings which are included in other warning  and avoid that the same meeting is present twice or more in the same warning
   public void cleanWarnings(String uid){
       List<Warning> warnings = warningFacade.getWarningsFromUID(Integer.parseInt(uid));
       
       for(Warning w: warnings){
           
           //this checks if a warning is totally included in other warning
           for(Warning w1 : warnings){
               if(w != w1 && w.getMeetings().contains(w1.getMeetings()) && w.getBreaks().contains(w1.getBreaks())){
                   warningFacade.remove(w1);
               }
           }
           
           String[] meets = w.getMeetings().split("%");
           String niu ="";
           //avoid that a warning contains twice the same meeting
           for(int i=0;i<meets.length;i++){
               for(int j=i+1;j<meets.length;j++){
                   if(meets[i].equals(meets[j])){
                       meets[i] = "";
                   }
               }
               if(!meets[i].equals(""))
               niu = niu.concat(meets[i].concat("%"));
           }  
           w.setMeetings(niu);
       }
   }
}
    


