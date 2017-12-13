/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import entities.Travelmean;
import java.io.IOException;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import sessionbeans.TravelmeanFacadeLocal;

/**
 *
 * @author Mirko
 */
public class ModifyTravelMeans extends HttpServlet {

    @EJB
    private TravelmeanFacadeLocal travelmeanFacade;

    

    
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
        
        
        Travelmean travel = travelmeanFacade.find(Integer.parseInt(uid));

        

        travel.setOwnedcar(request.getParameter("oc")!=null ? true : false);
        travel.setSharedcar(request.getParameter("sc")!=null ? true : false);
        travel.setOwnedbike(request.getParameter("ob")!=null ? true : false);
        travel.setSharedbike(request.getParameter("sb")!=null ? true : false);
        travel.setWalking(request.getParameter("w")!=null ? true : false);
        travel.setPublictransport(request.getParameter("p")!=null ? true : false);

        travelmeanFacade.edit(travel);
        
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

}
