/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import entities.Break;
import entities.Meeting;
import java.io.IOException;
import java.util.List;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import sessionbeans.BreakFacadeLocal;
import sessionbeans.MeetingFacadeLocal;

/**
 *
 * @author Mirko
 */
public class RecomputeCalendarMeetingsBreaks extends HttpServlet {

    @EJB
    private BreakFacadeLocal breakFacade;

    @EJB
    private MeetingFacadeLocal meetingFacade;

    
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String uid="none";
        HttpSession session = request.getSession();
        try{
       
        uid = session.getAttribute("uid").toString();
        
        }catch(NullPointerException e){
            response.sendRedirect("login.jsp");
        }
        
       List<Meeting> meetings = meetingFacade.getMeetingsFromUID(Integer.parseInt(uid));
       
       List<Break> breaks = breakFacade.getBreaksFromUID(Integer.parseInt(uid));


                String mJSON=loginservlet.createMeetingJSON(meetings,breaks);
                //session.setAttribute("meeeets", "{" + System.lineSeparator() + "title: 'qqqqqq'," + System.lineSeparator() + "start: '2017-11-01'" + System.lineSeparator() + "}");
                session.setAttribute("meeeets",mJSON);
       
       
       
       response.sendRedirect("Home");
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
