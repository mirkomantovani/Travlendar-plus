<%-- 
    Document   : routeview
    Created on : 30-Nov-2017, 22:39:23
    Author     : matteo
--%>

<%@page import="sessionbeans.showDirectionsMap"%>
<%@page import="sessionbeans.MeetingFacade"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Hello World!</h1>
    </body>
    <jsp:useBean id="showDirectionsMap" scope ="page"  class="showDirectionsMap"/>
    <% String query;
        query = showDirectionsMap.queryBuilder("via ungaretti,Peschiera Borromeo", "via Turoldo,Bussero", null); %>
    <iframe
  width="600"
  height="450"
  frameborder="0" style="border:0"
  src=query allowfullscreen
</iframe>
    
    
</html>
