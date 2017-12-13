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
        <title>Travlendar+ Sign up</title>
        <link rel="stylesheet" href="css/style.css">
    </head>
    <body>
        
        <form action="signupservlet" class="login" method="post">
            <h1>Travlendar+ Sign up </h1>
            <table border="3" cellpadding="2">
                
                <tbody>
                    <tr>
                        
                        <td><input type="text" name="name" value="" placeholder="Insert name *" class="login-input" /></td>
                    </tr>
                    <tr>
                        
                        <td><input type="text" placeholder="Insert surname" class="login-input"  name="surname" value="" /></td>
                    </tr>
                    <tr>
                        
                        <td><input type="email" placeholder="Insert email *" class="login-input"  name="email" value="" /></td>
                    </tr>
                    <tr>
                        
                        <td><input type="password" placeholder="Insert password *" class="login-input"  name="password" value="" /></td>
                    </tr>
                    <tr>
                        
                        <td><input type="password" placeholder="Confirm password *" class="login-input"  name="passwordconfirm" value="" /></td>
                    </tr>
                   
                </tbody>
               
            </table>
             <input type="submit" value="Sign up" class="login-submit"/>
             
             <p class="login-help"><a href="login.jsp">Already have an account? Login</a></p>
             
             <!--<a href="signup.jsp">go to sign up</a>
             <button type="submit" action="signup.jsp" value="Go to sign up" />
             -->

        </form>
    </body>
</html>
