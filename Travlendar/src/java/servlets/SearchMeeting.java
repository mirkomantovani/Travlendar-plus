/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import entities.Meeting;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
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
public class SearchMeeting extends HttpServlet {

    @EJB
    private MeetingFacadeLocal meetingFacade;

   

    
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
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
        
        List<Meeting> meetings =meetingFacade.getMeetingsFromNameAndUID(request.getParameter("meetingname").toLowerCase(),Integer.parseInt(uid));
        
        if(meetings.size()!=0){
        response.sendRedirect("MeetingVisualization?MeetingID="+meetings.get(0).getMeetingPK().getMeetingid());
        }
        else{
            response.sendRedirect("Home");
        }
        
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
