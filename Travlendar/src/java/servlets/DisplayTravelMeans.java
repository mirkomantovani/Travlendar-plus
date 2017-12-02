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
public class DisplayTravelMeans extends HttpServlet {

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
        
        HttpSession session = request.getSession();
        String uid = session.getAttribute("uid").toString();
        
        Travelmean travel = travelmeanFacade.find(Integer.parseInt(uid));
      
        
         
        //List<Product> products = productService.list(); // Obtain all products.
        request.setAttribute("oc", travel.getOwnedcar()? "checked" : ""); // Store var in request scope.
        request.setAttribute("sc", travel.getSharedcar()? "checked" : ""); // Store var in request scope.
        request.setAttribute("ob", travel.getOwnedbike()? "checked" : ""); // Store var in request scope.
        request.setAttribute("sb", travel.getSharedbike()? "checked" : ""); // Store var in request scope.
        request.setAttribute("w", travel.getWalking()? "checked" : ""); // Store var in request scope.
        request.setAttribute("p", travel.getPublictransport()? "checked" : ""); // Store var in request scope.
  
        request.getRequestDispatcher("selectTravelMeans.jsp").forward(request, response); // Forward to JSP page to display them in a HTML form
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
