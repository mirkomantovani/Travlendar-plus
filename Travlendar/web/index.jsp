<%-- 
    Document   : index.jsp
    Created on : 26-nov-2017, 22.22.10
    Author     : Mirko
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Sign up</title>
    </head>
    <body>
        <form action="loginservlet">
            <table border="3" cellpadding="2">
                
                <tbody>
                    <tr>
                        <td>Email</td>
                        <td><input type="text" name="email" value="" /></td>
                    </tr>
                    <tr>
                        <td>Password</td>
                        <td><input type="password" name="password" value="" /></td>
                    </tr>
                </tbody>
               
            </table>
             <input type="submit" value="Login" />
             
             <!--<a href="signup.jsp">go to sign up</a>
             <button type="submit" action="signup.jsp" value="Go to sign up" />
             -->

        </form>
         
         <form>
    <button formaction="signup.jsp">Go to sign up page</button>
    <button formaction="login.jsp">Go to real login page!!!</button>
    </form>
    </body>
</html>
