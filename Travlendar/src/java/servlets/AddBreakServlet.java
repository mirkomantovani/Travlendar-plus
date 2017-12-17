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
import java.util.Calendar;
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
    private BreakFacadeLocal breakFacade;

    

    
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
        
        String name = request.getParameter("name");
        String from=request.getParameter("from");
        String to=request.getParameter("to");
        String duration=request.getParameter("duration");
        Boolean rec= request.getParameter("recurrent")==null? false : true;        
        
        if(!name.equals("")&&!from.equals("")&&!to.equals("")&&!duration.equals("")&&areTimesValid(from,to,duration)){
            try{
                
               
                
        
        if(request.getParameter("mon")!=null)
            createBreak(Integer.parseInt(uid),name,addDateToTime(DateConversion.parseTime(from),2),DateConversion.parseTime(to),DateConversion.parseTime(duration),"monday",rec);
        if(request.getParameter("tue")!=null)
            createBreak(Integer.parseInt(uid),name,addDateToTime(DateConversion.parseTime(from),3),DateConversion.parseTime(to),DateConversion.parseTime(duration),"tuesday",rec);
        if(request.getParameter("wed")!=null)
            createBreak(Integer.parseInt(uid),name,addDateToTime(DateConversion.parseTime(from),4),DateConversion.parseTime(to),DateConversion.parseTime(duration),"wednesday",rec);
        if(request.getParameter("thu")!=null)
            createBreak(Integer.parseInt(uid),name,addDateToTime(DateConversion.parseTime(from),5),DateConversion.parseTime(to),DateConversion.parseTime(duration),"thursday",rec);
        if(request.getParameter("fri")!=null)
            createBreak(Integer.parseInt(uid),name,addDateToTime(DateConversion.parseTime(from),6),DateConversion.parseTime(to),DateConversion.parseTime(duration),"friday",rec);
        if(request.getParameter("sat")!=null)
            createBreak(Integer.parseInt(uid),name,addDateToTime(DateConversion.parseTime(from),7),DateConversion.parseTime(to),DateConversion.parseTime(duration),"saturday",rec);
        if(request.getParameter("sun")!=null)
            createBreak(Integer.parseInt(uid),name,addDateToTime(DateConversion.parseTime(from),1),DateConversion.parseTime(to),DateConversion.parseTime(duration),"sunday",rec);
         
        response.sendRedirect("RecomputeCalendarMeetingsBreaks");

        
        }catch(Exception e){
            response.sendRedirect("addBreak.jsp");
        }
        }
        else {
            response.sendRedirect("addBreak.jsp");
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

    private void createBreak(int parseInt, String name, Date from, Date to, Date duration, String day,Boolean recurrent) {
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

    private boolean areTimesValid(String from, String to, String duration) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        Date f=DateConversion.parseTime(from);
        Date t=DateConversion.parseTime(to);
        Date d=DateConversion.parseTime(duration);
        
        if(f.after(t)||f.equals(t)){
            System.out.println("from after to or from equal to");
            return false;
        }
        if(((t.getHours()-f.getHours())*60+(t.getMinutes()-f.getMinutes()))<(d.getHours()*60+d.getMinutes())){
            System.out.println("not enought space to min duration");
            return false;
        }
        return true;
    }

    private Date addDateToTime(Date parseTime,int dOfWeek) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
     //giving the break an actual date which is the first occurence of that day of week
                
                    Calendar calendar = Calendar.getInstance();
                    
            
            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
            
            int daysToGo=dOfWeek-dayOfWeek;
            
            if(daysToGo<0)
                daysToGo+=7;
            
             calendar.add(Calendar.DAY_OF_MONTH, daysToGo);
            
            Date d=calendar.getTime();
            parseTime.setDate(d.getDate());
            parseTime.setMonth(d.getMonth());
            parseTime.setYear(d.getYear());
            return parseTime;
    }

}
