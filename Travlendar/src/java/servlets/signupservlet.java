/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import entities.Preferences;
import entities.Travelmean;
import entities.Usertable;
import java.io.IOException;
import java.util.Date;
import java.util.Random;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import sessionbeans.PreferencesFacadeLocal;
import sessionbeans.TravelmeanFacadeLocal;
import sessionbeans.UsertableFacadeLocal;
import utils.SecureHashEncryption;

/**
 *
 * @author Mirko
 */
@WebServlet(name = "signupservlet", urlPatterns = {"/signupservlet"})
public class signupservlet extends HttpServlet {

    @EJB
    private TravelmeanFacadeLocal travelmeanFacade;

    @EJB
    private PreferencesFacadeLocal preferencesFacade;

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
        String surname = request.getParameter("surname");
        String passwordConfirm = request.getParameter("passwordconfirm");
        String password = request.getParameter("password");
        String email = request.getParameter("email");
        
       Random r=new Random();
        Usertable u=userFacade.find(email.hashCode());
        if(u==null &&password.equals(passwordConfirm)){
            createUser(email, username, surname, password, response);
        }
        else {
            response.sendRedirect("signup.jsp");
            //or response.sendRedirect("signup.jsp?error"); and then I can take the parameter and display error
            
            
        }
        
        
    }

    private void createUser(String email, String username, String surname, String password, HttpServletResponse response) throws IOException {
        //OKAY, CREATE USER
        Usertable user=new Usertable();
        //user.setUid(r.nextInt(1000));
        user.setUid(email.hashCode());
        user.setEmail(email);
        user.setName(username);
        user.setSurname(surname);
        user.setHashedpassword(SecureHashEncryption.encryptPassword(password));
        
        Preferences pref=new Preferences();
        pref.setAvoidmotorways(false);
        pref.setAvoidtolls(false);
        pref.setUid(user.getUid());
        pref.setMaxcyclingdistance(Integer.MAX_VALUE);
        pref.setMaxwalkingdistance(Integer.MAX_VALUE);
        pref.setMinimizecarbonfootprint(false);
        //setting as default date 0, meaning midnight
        Date d=new Date(0L);
        pref.setNopublictransportationsafter(d);
        pref.setMaxcyclingdistance(10000);
        pref.setMaxwalkingdistance(5000);
        //pref.setUsertable(user);
        
        //user.setPreferences(pref);
        
        //System.out.println(user.getPreferences().getMaxcyclingdistance());
       Travelmean travel=new Travelmean();
       
       travel.setUid(user.getUid());
       travel.setOwnedbike(true);
       travel.setOwnedcar(true);
       travel.setPublictransport(true);
       travel.setWalking(true);
       travel.setSharedbike(true);
       travel.setSharedcar(true);
       
        
        userFacade.create(user);
        preferencesFacade.create(pref);
        travelmeanFacade.create(travel);
        
        
        
        response.sendRedirect("login.jsp");
    }

}
