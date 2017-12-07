/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import entities.Preferences;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import sessionbeans.PreferencesFacadeLocal;
import sessionbeans.RouteCalculatorBean;
import sessionbeans.showDirectionsMap;
import utils.DateConversion;

/**
 *
 * @author Mirko
 */
public class ModifyPreferences extends HttpServlet {

    @EJB
    private PreferencesFacadeLocal preferencesFacade;
    @EJB
    private RouteCalculatorBean routes;
    @EJB
    private showDirectionsMap dir;



    
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
    
        
        try {
            long prova = routes.retrieveDuration("Peschiera Borromeo","Bussero",uid);
            System.out.println(prova);
        } catch (MalformedURLException ex) {
           ex.printStackTrace();
        } catch (ParseException ex) {
            ex.printStackTrace();
        } catch (org.json.simple.parser.ParseException ex) {
           ex.printStackTrace();
        }
        
        try {
            String prova2=dir.queryBuilder("Peschiera Borromeo", "Bussero", uid);
        } catch (org.json.simple.parser.ParseException ex) {
            Logger.getLogger(ModifyPreferences.class.getName()).log(Level.SEVERE, null, ex);
        }

        String minCarbonFootprint = "";
        String avoidTolls = "";
        String avoidMotorways = "";
        String maxwalk;
        String maxcycl;
        String noPublicAfter;
        
        minCarbonFootprint = request.getParameter("mincarbonfootprint");
        avoidTolls = request.getParameter("avoidtolls");
        avoidMotorways = request.getParameter("avoidmotorways");
        
        maxwalk=request.getParameter("maxwalking");
        maxcycl=request.getParameter("maxcycling");
        
        noPublicAfter=request.getParameter("nopublicafter");
        
        System.out.println(DateConversion.parseTime(noPublicAfter));
        
        pref.setNopublictransportationsafter(DateConversion.parseTime(noPublicAfter));
        
        pref.setMaxwalkingdistance(Integer.parseInt(maxwalk));
        pref.setMaxcyclingdistance(Integer.parseInt(maxcycl));
        
        

        

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
        
        response.sendRedirect("Home");

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
