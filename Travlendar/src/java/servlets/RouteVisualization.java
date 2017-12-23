/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import entities.Meeting;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.json.simple.parser.ParseException;
import sessionbeans.MeetingFacadeLocal;
import sessionbeans.RouteCalculatorBean;
import sessionbeans.showDirectionsMap;

/**
 *
 * @author matteo
 */
@WebServlet(name = "RouteVisualization", urlPatterns = {"/RouteVisualization"})
public class RouteVisualization extends HttpServlet {

    @EJB
    private showDirectionsMap showDir;
    @EJB
    private MeetingFacadeLocal meetingFacade;
    @EJB
    private RouteCalculatorBean routeCalc;
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
            out.println("<title>Servlet RouteVisualization</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet RouteVisualization at " + request.getContextPath() + "</h1>");
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
        
        String origin = request.getParameter("origin");
        
        int mid = (int) session.getAttribute("mid");
        
        Meeting m = (Meeting) session.getAttribute("m");
           
        String path="";
        
        session.setAttribute("path", path);
        
        
        try {
            if(routeCalc.retrieveDuration(origin, m.getLocation(), uid, m.getStartingdate()) == -1 || origin.equals("")){
                response.sendRedirect("meetingView.jsp");
            }else{
            
             path = showDir.queryBuilder(origin, m.getLocation(), uid, m.getStartingdate());
              session.setAttribute("path", path);
        request.getRequestDispatcher("routeview.jsp").forward(request, response);
            }
        } catch (ParseException ex) {
        }   catch (MalformedURLException | java.text.ParseException ex) {
                Logger.getLogger(RouteVisualization.class.getName()).log(Level.SEVERE, null, ex);
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
