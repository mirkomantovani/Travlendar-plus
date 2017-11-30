/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prove;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Mirko
 */
public class Prove {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String d="12 November 2017 - 07:13";
        String da="2001.07.04 AD at 12:08:56 PDT";
        SimpleDateFormat fo = new SimpleDateFormat("dd MMMMM yyyy - HH:mm",Locale.US);
        SimpleDateFormat f = new SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss z");
        
        Date date=new Date();
        
        try {

            date = fo.parse(d);
            

        
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        System.out.println(date);
    }
    
}
