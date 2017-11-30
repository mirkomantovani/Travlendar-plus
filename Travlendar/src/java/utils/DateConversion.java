/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import servlets.AddMeetingServlet;

/**
 *
 * @author Mirko
 */
public class DateConversion {
    public static Date parseDateFromHTMLForm(String d){
        SimpleDateFormat formatter = new SimpleDateFormat("dd MMMMM yyyy - HH:mm",Locale.US);
        Date date=new Date();
        try {

            return formatter.parse(d);
        
        } catch (ParseException ex) {
            Logger.getLogger(AddMeetingServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public static Timestamp parseTimestampFromHTMLForm(String d){
      return new Timestamp(DateConversion.parseDateFromHTMLForm(d).getTime());  
    }
    
    
}
