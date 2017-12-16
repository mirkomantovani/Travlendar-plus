/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import entities.Break;
import entities.BreakPK;
import entities.Meeting;
import entities.MeetingPK;
import entities.Warning;
import static entities.Warning_.breaks;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import sessionbeans.BreakFacadeLocal;
import sessionbeans.MeetingFacadeLocal;
import sessionbeans.WarningFacadeLocal;
import utils.WarningDetails;

/**
 *
 * @author matteo
 */
@WebServlet(name = "ConflictVisualization", urlPatterns = {"/ConflictVisualization"})
public class ConflictVisualization extends HttpServlet {

    @EJB
    private WarningFacadeLocal warningFacade;
    @EJB
    private MeetingFacadeLocal meetingFacade;
    @EJB
    private BreakFacadeLocal breakFacade;
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
            out.println("<title>Servlet ConflictVisualization</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ConflictVisualization at " + request.getContextPath() + "</h1>");
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
        
        HttpSession session = request.getSession();
        String uid = session.getAttribute("uid").toString(); 
        
      List<Warning> wList = new ArrayList<>();
     
      List<WarningDetails> warnings = new ArrayList<WarningDetails>();
   
      
      if(!warningFacade.getWarningsFromUID(Integer.parseInt(uid)).isEmpty()){
         wList = (List<Warning>) warningFacade.getWarningsFromUID(Integer.parseInt(uid));
        
         session.setAttribute("warnings", wList);
         
        for(Warning w: wList){
             ArrayList<Meeting> mList = new ArrayList<>();
             ArrayList<Break> bList = new ArrayList<>();
             
            if(!w.getMeetings().equals("")){
            String[] Ms = w.getMeetings().split("%");
            for(int i=0; i<Ms.length; i++){
                mList.add(meetingFacade.find(new MeetingPK(Integer.parseInt(uid),Integer.parseInt(Ms[i]))));
            }
            }
            if(!w.getBreaks().equals("")){
                String[] Bs = w.getBreaks().split("%");
                for(int j=0; j<Bs.length;j++){
                    bList.add(breakFacade.find(new BreakPK(Integer.parseInt(uid),Integer.parseInt(Bs[j]))));
                }
            }
            
            warnings.add(new WarningDetails(w,mList,bList));
        }
         
        session.setAttribute("warnDetails", warnings);
     
      }else{
         session.setAttribute("error","NO Warnings detected");
         session.setAttribute("warnDetails", warnings);
      }
     
      request.getRequestDispatcher("Conflicts.jsp").forward(request, response);
        
        
        
        
        
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
    
    private String[] extractMeetingsFromWarning(Warning w){
        String meetings = w.getMeetings();
        
        String[] result = new String[5];
   
        result = meetings.split("%");
        return result;
    
    }
    
    private String[] extractBreaksFromWarning(Warning w){
        String breaks = w.getBreaks();
        
        String[] result = new String[5];
        
        result = breaks.split("%");
        return result;
    }
    

    



}

 




