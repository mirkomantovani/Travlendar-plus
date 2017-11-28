<%-- 
    Document   : signup.jsp
    Created on : 27-nov-2017, 20.32.08
    Author     : Mirko
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Signu up Page</title>
    </head>
    <body>
        <h1>Sign up page</h1>
        <form action="signupservlet">
            <table border="3" cellpadding="2">
                
                <tbody>
                    <tr>
                        <td>Name</td>
                        <td><input type="text" name="name" value="" /></td>
                    </tr>
                    <tr>
                        <td>Password</td>
                        <td><input type="password" name="password" value="" /></td>
                    </tr>
                    <tr>
                        <td>Email</td>
                        <td><input type="text" name="email" value="" /></td>
                    </tr>
                </tbody>
               
            </table>
             <input type="submit" value="Sign up" />
             
             <!--<a href="signup.jsp">go to sign up</a>
             <button type="submit" action="signup.jsp" value="Go to sign up" />
             -->

        </form>
    </body>
</html>
