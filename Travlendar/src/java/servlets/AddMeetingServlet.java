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
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import sessionbeans.BreakFacadeLocal;
import sessionbeans.ConflictCheckerBean;
import sessionbeans.MeetingFacadeLocal;
import utils.DateConversion;

/**
 *
 * @author Mirko
 */
@WebServlet(name = "AddMeetingServlet", urlPatterns = {"/AddMeetingServlet"})
public class AddMeetingServlet extends HttpServlet {

    @EJB
    private MeetingFacadeLocal meetingFacade;
    @EJB
    private ConflictCheckerBean conflictChecker;

    
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session=request.getSession();
        
        Meeting m=new Meeting();
        MeetingPK mpk=new MeetingPK();
        
        String name=request.getParameter("name");
        String duration=request.getParameter("duration");
        String location=request.getParameter("location");
        String date=request.getParameter("date");
        
        if(!name.equals("")&&!duration.equals("")&&!location.equals("")&&!date.equals("")){
        try{
        mpk.setMeetingid(name.hashCode());

        
        String uid=session.getAttribute("uid").toString();
        
        
        mpk.setUid(Integer.parseInt(uid));
        
        m.setMeetingPK(mpk);
        m.setName(name);
        m.setDuration(Integer.parseInt(duration));
        m.setLocation(location);
        
        Timestamp tstamp = DateConversion.parseTimestampFromHTMLForm(date);
        
        m.setStartingdate(tstamp);
        meetingFacade.create(m);
        
       // System.out.println(date);
       // System.out.println(tstamp);
       
       Boolean conflitto;
       
       conflitto = conflictChecker.CheckAllConflicts(m);
       
       System.out.println(conflitto.toString());
       
       response.sendRedirect("RecomputeCalendarMeetingsBreaks");
       
        }catch(Exception e){
            response.sendRedirect("addmeeting.jsp");
        }
        } else{
             response.sendRedirect("addmeeting.jsp");
        }
        
        
    }

    

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

   

}
