/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import entities.Break;
import entities.Meeting;
import entities.Warning;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author matteo
 */
 public  class WarningDetails implements Serializable{
     
        private static final long serialVersionUID = 1L;
        
       private Warning w;
       private List<Meeting> meetings;
       private List<Break> breaks;
        
        public WarningDetails(Warning w, ArrayList<Meeting> meetings, ArrayList<Break> breaks){
            this.breaks = breaks;
            this.meetings = meetings;
            this.w = w;
        }

    public Warning getW() {
        return w;
    }

    public List<Meeting> getMeetings() {
        return meetings;
    }

    public List<Break> getBreaks() {
        return breaks;
    }

    public void setW(Warning w) {
        this.w = w;
    }

    public void setMeetings(List<Meeting> meetings) {
        this.meetings = meetings;
    }

    public void setBreaks(List<Break> breaks) {
        this.breaks = breaks;
    }
    
    
        
    
        
        
    }
