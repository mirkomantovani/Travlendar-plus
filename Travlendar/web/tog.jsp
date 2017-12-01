<%-- 
    Document   : tog
    Created on : 1-dic-2017, 12.08.06
    Author     : Mirko
--%>

<%@page import="entities.Preferences"%>
<%@page import="sessionbeans.PreferencesFacadeLocal"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <link rel="stylesheet" href="css/simple.css">
        <link rel="stylesheet" href="css/button.css">
    </head>
    <body>
        <jsp:useBean id="preferencesFacade" scope="session" class="sessionbeans.PreferencesFacadeLocal"/>
        <%
            
            if(session.getAttribute("uid")==null){
                response.sendRedirect("login.jsp");
            }
            else{

            Preferences pref =preferencesFacade.find(session.getAttribute("uid"));
            
            application.setAttribute( "pref", pref);
            
            }
            
            
            %>
        <form action="ModifyPreferences" >
        <div class="page">
  <div class="page__demo">
    <div class="main-container">
      <div class="page__container">  
          <h2>Minimize Carbon Footprint</h2>
        <label class="switch switch_type1" role="switch">
            
          <input type="checkbox" name="mincarbonfootprint" class="switch__toggle "<% 
                     Preferences pref =(Preferences)application.getAttribute("pref");
                     if(pref.getMinimizecarbonfootprint())
                     out.println("checked");
                     
                     
                     %> >
          <span class="switch__label"></span>
        </label>
          <h2>Avoid Tolls</h2>
        <label class="switch switch_type2" role="switch">
          <input type="checkbox" name="avoidtolls" class="switch__toggle "<% 
                     
                     if(pref.getAvoidtolls())
                     out.println("checked");
                     
                     
                     %>>
          <span class="switch__label"> </span>
        </label> 
          <h2>Avoid Motorways</h2>
        <label class="switch switch_type3" role="switch">
          <input type="checkbox" name="avoidmotorways" class="switch__toggle" <% 
                     
                     if(pref.getAvoidmotorways())
                     out.println("checked");
                     
                     
                     %> >
          <span class="switch__label"></span>
        </label>
          <br>
          <br>
          
          <button type="submit" class="offset">Submit</button>
      </div>
    </div>
  </div>
  <footer class="footer">
   
  </footer>
</div>
        </form>
    </body>
</html>
