/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import entities.Meeting;
import entities.MeetingPK;
import entities.Warning;
import java.io.IOException;
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

/**
 *
 * @author Mirko
 */
public class DeleteMeeting extends HttpServlet {

    @EJB
    private MeetingFacadeLocal meetingFacade;
    @EJB
    private WarningFacadeLocal warningFacade;
    @EJB
    private ConflictCheckerBean checker;

   
    
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
        
        Meeting m = meetingFacade.find(mpk);
        
        List<Warning> warnings = warningFacade.getWarningsFromUID(Integer.parseInt(uid));
        
        //A meno che il conflitto sia tipo m-m o m-b, ricalcolo i conflitti per tutti i meeting coinvolti ->
        
        

        meetingFacade.remove(meetingFacade.find(mpk));
        
         for(Warning w: warnings){
           if(w.getMeetings().contains(String.valueOf(m.getMeetingPK().getMeetingid()))){
               String[] meets;
               String[] bees;
               String mflag;
               String bflag;
               
               mflag = w.getMeetings().replace(String.valueOf(m.getMeetingPK().getMeetingid()).concat("%"), "");
               System.out.println("mflag:" + mflag);
               bflag = w.getBreaks();
               meets = mflag.split("%");
               bees = bflag.split("%");
               warningFacade.remove(w);
               if(meets.length>1 || bees.length>=1 ){
               for(int i=0;i<meets.length;i++){
                   System.out.println("meets di i:" + meets[i]);
                   MeetingPK mPK = new MeetingPK();
                   mPK.setMeetingid(Integer.parseInt(meets[i]));
                   mPK.setUid(Integer.parseInt(uid));
                   checker.CheckAllConflicts(meetingFacade.find(mPK));
               }
               }
           }
         }
        
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
