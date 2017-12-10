/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import entities.Break;
import entities.Meeting;
import entities.MeetingPK;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.List;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import sessionbeans.BreakFacadeLocal;
import sessionbeans.MeetingFacadeLocal;
import utils.DateConversion;

/**
 *
 * @author Mirko
 */
public class updateMeetingServlet extends HttpServlet {

    

    @EJB
    private MeetingFacadeLocal meetingFacade;
    
    

    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    HttpSession session=request.getSession();
        
        try{
        MeetingPK mpk=new MeetingPK();
        
        mpk.setMeetingid(request.getParameter("name").hashCode());
        String uid=session.getAttribute("uid").toString();
        
        
        mpk.setUid(Integer.parseInt(uid));
        
        Meeting m=meetingFacade.find(mpk);
        
        m.setDuration(Integer.parseInt(request.getParameter("duration")));
        
        m.setLocation(request.getParameter("location"));
        
        Timestamp tstamp = DateConversion.parseTimestampFromHTMLForm(request.getParameter("date"));
        
        m.setStartingdate(tstamp);
        meetingFacade.edit(m);
        }catch(Exception e){
            response.sendRedirect("updateMeeting.jsp");
        }
        
       // System.out.println(date);
       // System.out.println(tstamp);
       
       
       response.sendRedirect("RecomputeCalendarMeetingsBreaks");
    }

   

}
