/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import entities.BreakPK;
import java.io.IOException;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import sessionbeans.BreakFacadeLocal;

/**
 *
 * @author Mirko
 */
public class DeleteBreak extends HttpServlet {

    @EJB
    private BreakFacadeLocal breakFacade;

    
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
       
        
        String bidd=request.getParameter("BreakID");
        
        System.out.println(bidd);
        
        int breakID = Integer.parseInt(request.getParameter("BreakID"));
        
        
        BreakPK bpk= new BreakPK();
        bpk.setBreakid(breakID);
        bpk.setUid(Integer.parseInt(uid));
        

        breakFacade.remove(breakFacade.find(bpk));
        
        response.sendRedirect("RecomputeCalendarMeetingsBreaks");
    }

    
}
