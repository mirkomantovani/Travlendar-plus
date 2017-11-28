<%-- 
    Document   : homepage
    Created on : 27-nov-2017, 21.55.38
    Author     : Mirko
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Homepage</title>
    </head>
    <body>
        <%
            if(session.getAttribute("name")==null){
                response.sendRedirect("login.jsp");
            }
            
            %>
        <h1>Welcome to the homepage ${name}</h1>
    </body>
</html>
