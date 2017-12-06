/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import entities.Meeting;
import entities.Usertable;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import sessionbeans.MeetingFacadeLocal;
import sessionbeans.UsertableFacadeLocal;
import utils.SecureHashEncryption;

/**
 *
 * @author Mirko
 */
public class loginservlet extends HttpServlet {

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

        //INSERTED BY ME
        /*
        Utente user=new Utente();
        user.setId(id);
        this.id++;
        user.setUsername(request.getParameter("name"));
        user.setPassword(request.getParameter("password"));
        utenteFacade.create(user);
         */
        System.out.println("dieee");
        Usertable u = userFacade.find(request.getParameter("email").hashCode());
        String password = request.getParameter("password");

        System.out.println(password);
        System.out.println(u.getHashedpassword());

        if (u == null) {
            response.sendRedirect("signup.jsp");
            System.out.println("user not in database");
        } else {
            if (SecureHashEncryption.encryptPassword(password).equals(u.getHashedpassword())) {

                HttpSession session = request.getSession();
                session.setAttribute("name", u.getName());

                session.setAttribute("user", u);


                //GETTING EVERY MEETING THE USER HAS IN ORDER TO DISPLAY THEM IN THE CALENDAR HOMEPAGE
                List<Meeting> meetings = meetingFacade.getMeetingsFromUID(u.getUid());


                String mJSON=createMeetingJSON(meetings);
                //session.setAttribute("meeeets", "{" + System.lineSeparator() + "title: 'qqqqqq'," + System.lineSeparator() + "start: '2017-11-01'" + System.lineSeparator() + "}");
                session.setAttribute("meeeets",mJSON);
                
                
                //WHEN THE USER LOGS IN WE HAVE TO CHECK WHETHER THERE ARE WARNINGS, IF THERE ARE EXECUTE THIS LINE
                //session.setAttribute("warningcolor", "red");

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

        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet loginservlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>You successfully submitted your data into the database: name= " + request.getParameter("name") + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

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

    
    
    public static String createMeetingJSON(List<Meeting> meetings) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        //session.setAttribute("meeeets", "{" + System.lineSeparator() + "title: 'qqqqqq'," + System.lineSeparator() + "start: '2017-11-01'" + System.lineSeparator() + "}");

        //the size of the string builder is going to be based on the number of meetings in order to enhance performances
        StringBuilder sb = new StringBuilder(meetings.size()*50);
        
        //for(Meeting m : meetings){
        for(int i=0;i<meetings.size();i++){
            if(i!=0){
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
        sb.append("url: '/Travlendar/DisplayMeeting?meetingid=");
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
        sb.append(getTimeAsString(getEndingDate(meetings.get(i).getStartingdate(),meetings.get(i).getDuration())));
        sb.append("'");
        
        sb.append(System.lineSeparator());
        sb.append("}");
        
        }
        
        return sb.toString();
    }

    private static String getTimeAsString(Date d) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        StringBuilder sb = new StringBuilder();
        int h=d.getHours();
        if(h<10){
            sb.append("0");
        }
        sb.append(h);
        sb.append(":");
        int m=d.getMinutes();
        if(m<10){
            sb.append("0");
        }
        sb.append(m);
        sb.append(":00");
       
        
        return sb.toString();
    }
    
    private static String getDateAsString(Date d) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        StringBuilder sb = new StringBuilder();
        sb.append(d.getYear()+1900);
        sb.append("-");
        int month=d.getMonth()+1;
        if(month<10){
            sb.append("0");
        }
        sb.append(month);
        sb.append("-");
        int day=d.getDate();
        if(day<10){
            sb.append("0");
        }
        sb.append(day);
       
        
        return sb.toString();
    }

    private static Date getEndingDate(Date startingdate, Integer duration) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        Date d=startingdate;
        
        int hours=(int)duration/60;
        int minutes=duration%60;
        if(d.getHours()+hours<24){
        d.setHours(d.getHours()+hours);
        d.setMinutes(d.getMinutes()+minutes);
        }
        else {
            d.setHours(23);
            d.setMinutes(59);
        }
        return d;
    }

}
