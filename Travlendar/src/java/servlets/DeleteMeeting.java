/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import entities.MeetingPK;
import java.io.IOException;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import sessionbeans.MeetingFacadeLocal;

/**
 *
 * @author Mirko
 */
public class DeleteMeeting extends HttpServlet {

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
        
        int meetingID=Integer.parseInt(request.getParameter("meetingid"));
        //int meetingID = Integer.parseInt(request.getParameter("MeetingID"));
        
        MeetingPK mpk= new MeetingPK();
        mpk.setMeetingid(meetingID);
        mpk.setUid(Integer.parseInt(uid));
        

        meetingFacade.remove(meetingFacade.find(mpk));
        
        response.sendRedirect("RecomputeCalendarMeetingsBreaks");
        
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
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
