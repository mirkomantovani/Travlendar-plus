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
import sessionbeans.MeetingFacadeLocal;
import utils.DateConversion;

/**
 *
 * @author Mirko
 */
@WebServlet(name = "AddMeetingServlet", urlPatterns = {"/AddMeetingServlet"})
public class AddMeetingServlet extends HttpServlet {

    @EJB
    private BreakFacadeLocal breakFacade;

    @EJB
    private MeetingFacadeLocal meetingFacade;

    
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet AddMeetingServlet</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet AddMeetingServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session=request.getSession();
        
        Meeting m=new Meeting();
        MeetingPK mpk=new MeetingPK();
        
        mpk.setMeetingid(request.getParameter("name").hashCode());

        
        String uid=session.getAttribute("uid").toString();
        
        
        mpk.setUid(Integer.parseInt(uid));
        
        m.setMeetingPK(mpk);
        m.setName(request.getParameter("name"));
        
        m.setDuration(Integer.parseInt(request.getParameter("duration")));
        
        m.setLocation(request.getParameter("location"));
        
        Timestamp tstamp = DateConversion.parseTimestampFromHTMLForm(request.getParameter("date"));
        
        m.setStartingdate(tstamp);
        meetingFacade.create(m);
        
       // System.out.println(date);
       // System.out.println(tstamp);
       
       
       List<Meeting> meetings = meetingFacade.getMeetingsFromUID(Integer.parseInt(uid));
       
       List<Break> breaks = breakFacade.getBreaksFromUID(Integer.parseInt(uid));


                String mJSON=loginservlet.createMeetingJSON(meetings,breaks);
                //session.setAttribute("meeeets", "{" + System.lineSeparator() + "title: 'qqqqqq'," + System.lineSeparator() + "start: '2017-11-01'" + System.lineSeparator() + "}");
                session.setAttribute("meeeets",mJSON);
       
       
       
       response.sendRedirect("Home");
        
        
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
        processRequest(request, response);
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
