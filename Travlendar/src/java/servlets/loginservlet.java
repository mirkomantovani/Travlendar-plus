/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import entities.Usertable;
import java.io.IOException;
import java.io.PrintWriter;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import sessionbeans.UsertableFacadeLocal;
import utils.SecureHashEncryption;

/**
 *
 * @author Mirko
 */
public class loginservlet extends HttpServlet{

    
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
        Usertable u=userFacade.find(request.getParameter("email").hashCode());
        String password=request.getParameter("password");
        
        System.out.println(password);
        System.out.println(u.getHashedpassword());
        
        if(u==null){
            response.sendRedirect("signup.jsp");
            System.out.println("user not in database");
        }
        else {
            if(SecureHashEncryption.encryptPassword(password).equals(u.getHashedpassword())){
               
                HttpSession session=request.getSession();
                session.setAttribute("name", u.getName());
                session.setAttribute("uid", u.getUid());
                //session.setAttribute("name", u.getName());
                System.out.println("REDIRECTING TO HOMEPAGE JSP");
                response.sendRedirect("Home");
            }
            else{
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
            out.println("<h1>You successfully submitted your data into the database: name= "+request.getParameter("name")+"</h1>");
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

}
