/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import entities.Usertable;
import java.io.IOException;
import java.util.Random;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import sessionbeans.UsertableFacadeLocal;

/**
 *
 * @author Mirko
 */
@WebServlet(name = "signupservlet", urlPatterns = {"/signupservlet"})
public class signupservlet extends HttpServlet {

   @EJB
    private UsertableFacadeLocal userFacade;
    

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
        
        String username = request.getParameter("name");
        String password = request.getParameter("password");
        String email = request.getParameter("email");
        
       Random r=new Random();
        Usertable u=userFacade.find(email.hashCode());
        if(u==null){
            //OKAY, CREATE USER
            Usertable user=new Usertable();
            //user.setUid(r.nextInt(1000));
            user.setUid(email.hashCode());
        user.setEmail(email);
        user.setName(username);
        user.setHashedpassword(password);
        userFacade.create(user);
        response.sendRedirect("login.jsp");
        }
        else {
            response.sendRedirect("signup.jsp");
            //or response.sendRedirect("signup.jsp?error"); and then I can take the parameter and display error
            
            
        }
        
        
    }

}
