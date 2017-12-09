/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import entities.Meeting;
import entities.MeetingPK;
import java.io.IOException;
import java.io.PrintWriter;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import sessionbeans.MeetingFacadeLocal;

/**
 *
 * @author Mirko
 */
public class DisplayUpdateMeeting extends HttpServlet {

    @EJB
    private MeetingFacadeLocal meetingFacade;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        String uid = session.getAttribute("uid").toString();

        int mID=Integer.parseInt(request.getParameter("meetingid"));
        //int mID = Integer.parseInt(request.getParameter("MeetingID"));

        MeetingPK mPK = new MeetingPK();

        mPK.setMeetingid(mID);
        mPK.setUid(Integer.parseInt(uid));

        Meeting m = meetingFacade.find(mPK);

        session.setAttribute("updatem", m);

        request.getRequestDispatcher("updateMeeting.jsp").forward(request, response);
    }

}
