/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import entities.Break;
import entities.BreakPK;
import entities.Meeting;
import java.io.IOException;
import java.util.Date;
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
public class AddBreakServlet extends HttpServlet {

    @EJB
    private MeetingFacadeLocal meetingFacade;

    @EJB
    private BreakFacadeLocal breakFacade;

    

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
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
        
        HttpSession session=request.getSession();
        String uid=session.getAttribute("uid").toString();
        
        String name = request.getParameter("name");
        String from=request.getParameter("from");
        String to=request.getParameter("to");
        String duration=request.getParameter("duration");
        Boolean rec= request.getParameter("recurrent")==null? false : true;        
        
        
        if(request.getParameter("mon")!=null)
            createMeeting(Integer.parseInt(uid),name,DateConversion.parseTime(from),DateConversion.parseTime(to),DateConversion.parseTime(duration),"monday",rec);
        if(request.getParameter("tue")!=null)
            createMeeting(Integer.parseInt(uid),name,DateConversion.parseTime(from),DateConversion.parseTime(to),DateConversion.parseTime(duration),"tuesday",rec);
        if(request.getParameter("wed")!=null)
            createMeeting(Integer.parseInt(uid),name,DateConversion.parseTime(from),DateConversion.parseTime(to),DateConversion.parseTime(duration),"wednesday",rec);
        if(request.getParameter("thu")!=null)
            createMeeting(Integer.parseInt(uid),name,DateConversion.parseTime(from),DateConversion.parseTime(to),DateConversion.parseTime(duration),"thursday",rec);
        if(request.getParameter("fri")!=null)
            createMeeting(Integer.parseInt(uid),name,DateConversion.parseTime(from),DateConversion.parseTime(to),DateConversion.parseTime(duration),"friday",rec);
        if(request.getParameter("sat")!=null)
            createMeeting(Integer.parseInt(uid),name,DateConversion.parseTime(from),DateConversion.parseTime(to),DateConversion.parseTime(duration),"saturday",rec);
        if(request.getParameter("sun")!=null)
            createMeeting(Integer.parseInt(uid),name,DateConversion.parseTime(from),DateConversion.parseTime(to),DateConversion.parseTime(duration),"sunday",rec);
         
        
        List<Meeting> meetings = meetingFacade.getMeetingsFromUID(Integer.parseInt(uid));
       
       List<Break> breaks = breakFacade.getBreaksFromUID(Integer.parseInt(uid));


                String mJSON=loginservlet.createMeetingJSON(meetings,breaks);
                
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

    private void createMeeting(int parseInt, String name, Date from, Date to, Date duration, String day,Boolean recurrent) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        Break b=new Break();
        BreakPK bpk=new BreakPK();
        
        bpk.setUid(parseInt);
        bpk.setBreakid((parseInt+name+from+to+duration+day).hashCode());
        b.setBreakPK(bpk);
        b.setDayofweek(day);
        b.setStartingtime(from);
        b.setEndingtime(to);
        b.setMinduration(duration);
        b.setName(name);
        b.setRecurrent(recurrent);
        
        
        
        breakFacade.create(b);

    }

}
