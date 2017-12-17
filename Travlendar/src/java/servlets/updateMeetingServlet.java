/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import entities.Break;
import entities.Meeting;
import entities.MeetingPK;
import entities.Warning;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import sessionbeans.ConflictCheckerBean;
import sessionbeans.MeetingFacadeLocal;
import sessionbeans.WarningFacadeLocal;
import utils.DateConversion;

/**
 *
 * @author Mirko
 */
public class updateMeetingServlet extends HttpServlet {

    

    @EJB
    private MeetingFacadeLocal meetingFacade;
    @EJB
    private WarningFacadeLocal warningFacade;
    @EJB
    private ConflictCheckerBean checker;
    
    

    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    HttpSession session=request.getSession();
   
        
        try{
        MeetingPK mpk=new MeetingPK();
        
       // String n=request.getParameter("name");
        
        
        mpk.setMeetingid(Integer.parseInt(request.getParameter("MeetingID")));
        String uid=session.getAttribute("uid").toString();
        //System.out.println(mpk.getMeetingid());
        
        mpk.setUid(Integer.parseInt(uid));
        
        Meeting m=meetingFacade.find(mpk);
        
        m.setDuration(Integer.parseInt(request.getParameter("duration")));
        
        m.setLocation(request.getParameter("location"));
        
        Timestamp tstamp = DateConversion.parseTimestampFromHTMLForm(request.getParameter("date"));
        
        m.setStartingdate(tstamp);
        meetingFacade.edit(m);
        
        List<Warning> warnings = warningFacade.getWarningsFromUID(Integer.parseInt(uid));
        
       
           for(Warning w: warnings){
           if(w.getMeetings().contains(String.valueOf(m.getMeetingPK().getMeetingid()))){
               String[] meets;
               
               String mflag;
               mflag = w.getMeetings().replace(String.valueOf(m.getMeetingPK().getMeetingid()).concat("%"), "");
               System.out.println("mflag:" + mflag);
            
               meets = mflag.split("%");
               warningFacade.remove(w);
               
               for(int i=0;i<meets.length;i++){
                   System.out.println("meets di i:" + meets[i]);
                   MeetingPK mPK = new MeetingPK();
                   mPK.setMeetingid(Integer.parseInt(meets[i]));
                   mPK.setUid(Integer.parseInt(uid));
                   checker.CheckAllConflicts(meetingFacade.find(mPK));
               }
               
           }
         }
        
        }catch(NullPointerException e){
            response.sendRedirect("login.jsp");
        }
        catch(Exception e){
            response.sendRedirect("updateMeeting.jsp");
        }
        
       // System.out.println(date);
       // System.out.println(tstamp);
       
     
       
       
       response.sendRedirect("RecomputeCalendarMeetingsBreaks");
    }

   

}
