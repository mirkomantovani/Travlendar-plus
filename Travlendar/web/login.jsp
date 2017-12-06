<%-- 
    Document   : login
    Created on : 28-nov-2017, 21.57.01
    Author     : Mirko
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Travlendar+ login</title>
        <link rel="stylesheet" href="css/style.css">
    </head>
    <body>
       <form action="loginservlet" class="login" method="post">
           <h1>Travlendar+ Login</h1>
            <table border="3" cellpadding="2">
                
                <tbody>
                    <tr>
                        
                        <td><input type="text" placeholder="Insert email" name="email" value="" class="login-input"/></td>
                    </tr>
                    <tr>
                        
                        <td><input type="password" placeholder="Insert password" name="password" value="" class="login-input" /></td>
                    </tr>
                </tbody>
               
            </table>
             <input type="submit" value="Login" class="login-submit" />
             <p class="login-help"><a href="signup.jsp">Don't have an account? Sign up</a></p>
              <p class="login-help"><a href="index.html">Forgot password?</a></p>
             
             <!--<a href="signup.jsp">go to sign up</a>
             <button type="submit" action="signup.jsp" value="Go to sign up" />
             -->

        </form>
    </body>
</html>
