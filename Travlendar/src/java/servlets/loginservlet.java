/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import entities.Break;
import entities.Meeting;
import entities.Usertable;
import entities.Warning;
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
import sessionbeans.UsertableFacadeLocal;
import sessionbeans.WarningFacadeLocal;
import utils.SecureHashEncryption;

/**
 *
 * @author Mirko
 */
public class loginservlet extends HttpServlet {

    @EJB
    private WarningFacadeLocal warningFacade;

    private final static int breaksPrecomputation=8;

    @EJB
    private BreakFacadeLocal breakFacade;

    @EJB
    private MeetingFacadeLocal meetingFacade;

    @EJB
    private UsertableFacadeLocal userFacade;
    
    

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

       if(!request.getParameter("email").equals("")&&!request.getParameter("password").equals("")){
        Usertable u = userFacade.find(request.getParameter("email").hashCode());
        String password = request.getParameter("password");


        if (u == null) {
            response.sendRedirect("signup.jsp");
            
        } else {
            if (SecureHashEncryption.encryptPassword(password).equals(u.getHashedpassword())) {

                HttpSession session = request.getSession();
                session.setAttribute("name", u.getName());

                session.setAttribute("user", u);
                
                //WHEN THE USER LOGS IN WE HAVE TO CHECK WHETHER THERE ARE WARNINGS, IF THERE ARE EXECUTE THIS LINE
                
                List<Warning> warnings = warningFacade.getWarningsFromUID(u.getUid());
                
                if(warnings.size()>0)
                    session.setAttribute("warningcolor", "red");
                    

                //GETTING EVERY MEETING THE USER HAS IN ORDER TO DISPLAY THEM IN THE CALENDAR HOMEPAGE
                List<Meeting> meetings = meetingFacade.getMeetingsFromUID(u.getUid());

                List<Break> breaks = breakFacade.getBreaksFromUID(u.getUid());
                
                

                String mJSON = createMeetingJSON(meetings, breaks);
                //session.setAttribute("meeeets", "{" + System.lineSeparator() + "title: 'qqqqqq'," + System.lineSeparator() + "start: '2017-11-01'" + System.lineSeparator() + "}");
                session.setAttribute("meeeets", mJSON);

                
                //for(Meeting me: meetings)
                //System.out.println(me.getName());
                session.setAttribute("uid", u.getUid());
                //session.setAttribute("name", u.getName());
                System.out.println("REDIRECTING TO HOMEPAGE JSP");
                response.sendRedirect("Home");
            } else {
                System.out.println(SecureHashEncryption.encryptPassword(password));
                System.out.println(u.getHashedpassword());
                System.out.println("REDIRECTING TO INDEX JSP");
                response.sendRedirect("index.jsp");
            }
        }
       }
       else{
           response.sendRedirect("login.jsp");
       }

        
    }

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
        processRequest(request, response);
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

    public static String createMeetingJSON(List<Meeting> meetings, List<Break> breaks) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        //session.setAttribute("meeeets", "{" + System.lineSeparator() + "title: 'qqqqqq'," + System.lineSeparator() + "start: '2017-11-01'" + System.lineSeparator() + "}");

        //the size of the string builder is going to be based on the number of meetings in order to enhance performances
        StringBuilder sb = new StringBuilder(meetings.size() * 50);

        //for(Meeting m : meetings){
        for (int i = 0; i < meetings.size(); i++) {
            if (i != 0) {
                sb.append(",");
                sb.append(System.lineSeparator());
            }
            sb.append("{");
            sb.append(System.lineSeparator());
            sb.append("title: '");
            sb.append(meetings.get(i).getName());
            sb.append("',");
            sb.append(System.lineSeparator());

            //Here I'm going to define the URL of the meeting
            sb.append("url: '/Travlendar/MeetingVisualization?MeetingID=");
            sb.append(meetings.get(i).getMeetingPK().getMeetingid());
            sb.append("',");
            sb.append(System.lineSeparator());

            //START DATE
            sb.append("start: '");
            sb.append(getDateAsString(meetings.get(i).getStartingdate()));
            sb.append("T");
            sb.append(getTimeAsString(meetings.get(i).getStartingdate()));
            sb.append("',");

            //END DATE
            sb.append("end: '");
            sb.append(getDateAsString(meetings.get(i).getStartingdate()));
            sb.append("T");
            sb.append(getTimeAsString(getEndingDate(meetings.get(i).getStartingdate(), meetings.get(i).getDuration())));
            sb.append("'");

            sb.append(System.lineSeparator());
            sb.append("}");

        }
        
        int times;

        for (int i = 0; i < breaks.size(); i++) {
            //number of breaks to compute and put in the calendar in advance
            times=breaksPrecomputation;
            Calendar calendar= Calendar.getInstance();
            
            do{
                   
            if(times==breaksPrecomputation){
            calendar = Calendar.getInstance();
            
            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
            
            int daysToGo=getDayNumFromString(breaks.get(i).getDayofweek())-dayOfWeek;
            
            if(daysToGo<0)
                daysToGo+=7;
            
            System.out.println(daysToGo);
                    calendar.add(Calendar.DAY_OF_MONTH, daysToGo);
            }
             times--;       
                    
            Date d=calendar.getTime();

            //LocalDate thisMonday = startDate.withDayOfWeek(DateTimeConstants.MONDAY);
            Date start = breaks.get(i).getStartingtime();
            start.setDate(d.getDate());
            start.setMonth(d.getMonth());
            start.setYear(d.getYear());
            
            
            sb.append(",");
            sb.append(System.lineSeparator());
            sb.append("{");
            sb.append(System.lineSeparator());
            sb.append("title: 'Break-");
            sb.append(breaks.get(i).getName());
            sb.append("',");
            sb.append(System.lineSeparator());

            sb.append("url: '/Travlendar/DisplayBreak?breakid=");
            sb.append(breaks.get(i).getBreakPK().getBreakid());
            sb.append("',");
            sb.append(System.lineSeparator());

            
            //START DATE
            sb.append("start: '");
            sb.append(getDateAsString(start));
            sb.append("T");
            sb.append(getTimeAsString(start));
            sb.append("',");

            Date end = breaks.get(i).getEndingtime();
            end.setDate(10);
            end.setMonth(11);
            end.setYear(117);

            //END DATE
            sb.append("end: '");
            sb.append(getDateAsString(start));
            sb.append("T");
            sb.append(getTimeAsString(end));
            sb.append("',");
            sb.append(System.lineSeparator());

            sb.append("color: '#ed4b4b'");

            sb.append(System.lineSeparator());
            sb.append("}");
            
            calendar.add(Calendar.DAY_OF_MONTH, 7);
            }while(breaks.get(i).getRecurrent()&&times>0);

        }

        return sb.toString();
    }

    private static String getTimeAsString(Date d) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        StringBuilder sb = new StringBuilder();
        int h = d.getHours();
        if (h < 10) {
            sb.append("0");
        }
        sb.append(h);
        sb.append(":");
        int m = d.getMinutes();
        if (m < 10) {
            sb.append("0");
        }
        sb.append(m);
        sb.append(":00");

        return sb.toString();
    }

    private static String getDateAsString(Date d) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        StringBuilder sb = new StringBuilder();
        sb.append(d.getYear() + 1900);
        sb.append("-");
        int month = d.getMonth() + 1;
        if (month < 10) {
            sb.append("0");
        }
        sb.append(month);
        sb.append("-");
        int day = d.getDate();
        if (day < 10) {
            sb.append("0");
        }
        sb.append(day);

        return sb.toString();
    }

    private static Date getEndingDate(Date startingdate, Integer duration) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        Date d = new Date();
        
               
        int hours = (int) duration / 60;
        int minutes = duration % 60;
        if (startingdate.getHours() + hours < 24) {
            d.setHours(startingdate.getHours() + hours);
            d.setMinutes(startingdate.getMinutes() + minutes);
        } else {
            d.setHours(23);
            d.setMinutes(59);
        }
        return d;
    }
    
    private static int getDayNumFromString(String dayofweek) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    
        switch(dayofweek){
            case "sunday": return 1;
            case "monday": return 2;
            case "tuesday": return 3;
            case "wednesday": return 4;
            case "thursday": return 5;
            case "friday": return 6;
            case "saturday": return 7;
        }
        return 0;
        
        
    }

}
