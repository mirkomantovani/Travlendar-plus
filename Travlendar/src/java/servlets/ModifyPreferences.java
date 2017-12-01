/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import entities.Preferences;
import java.io.IOException;
import java.io.PrintWriter;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import sessionbeans.PreferencesFacadeLocal;

/**
 *
 * @author Mirko
 */
public class ModifyPreferences extends HttpServlet {

    @EJB
    private PreferencesFacadeLocal preferencesFacade;



    
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
        
        Preferences pref = preferencesFacade.find(Integer.parseInt(uid));

        String minCarbonFootprint = "";
        String avoidTolls = "";
        String avoidMotorways = "";

        minCarbonFootprint = request.getParameter("mincarbonfootprint");
        avoidTolls = request.getParameter("avoidtolls");
        avoidMotorways = request.getParameter("avoidmotorways");

        

        if (minCarbonFootprint!=null) {
            pref.setMinimizecarbonfootprint(true);
        } else {
            pref.setMinimizecarbonfootprint(false);
        }

        if (avoidTolls!=null) {
            pref.setAvoidtolls(true);
        } else {
            pref.setAvoidtolls(false);
        }

        if (avoidMotorways!=null) {
            pref.setAvoidmotorways(true);
        } else {
            pref.setAvoidmotorways(false);
        }

        preferencesFacade.edit(pref);

       // System.out.println(minCarbonFootprint);
        //System.out.println(avoidMotorways);
        //System.out.println(avoidTolls);

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