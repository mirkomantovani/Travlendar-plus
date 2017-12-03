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
         <link rel="stylesheet" href="css/navbar.css">
        <title>JSP Page</title>
    </head>
    <body>
     
         <!--          ----NAVBAR----         -->
        <nav class="navbar navbar-default navbar-inverse navbar-fixed-top" role="navigation">
            <div class="container-fluid">
                <!-- Brand and toggle get grouped for better mobile display -->
                <div class="navbar-header">
                    <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1">
                        <span class="sr-only">Toggle navigation</span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                    </button>
                    <a class="navbar-brand" href="homepage.jsp">TRAVLENDAR+</a>
                </div>

                <!-- Collect the nav links, forms, and other content for toggling -->
                <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
                    <ul class="nav navbar-nav">
                        
                        <li class="active">
                            <form action="DisplayPreferences">
                                <a href="#" onclick="$(this).closest('form').submit()">Modify preferences</a>
                            </form>
                        </li>
                    
                        <li>
                            <form action="DisplayTravelMeans">
                                <a href="#" onclick="$(this).closest('form').submit()">Select travel means</a>
                            </form>
                        </li>
                        
                        <li>
                            <form action="DisplayTravelMeans">
                                <a href="addmeeting.jsp" >Add meeting</a>
                            </form>
                        </li>
                        
                        <li class="dropdown">
                            <a href="#" class="dropdown-toggle" data-toggle="dropdown">Dropdown <span class="caret"></span></a>
                            <ul class="dropdown-menu" role="menu">
                                <li><a href="#">Action</a></li>
                                <li><a href="#">Another action</a></li>
                                <li><a href="#">Something else here</a></li>
                                <li class="divider"></li>
                                <li><a href="#">Separated link</a></li>
                                <li class="divider"></li>
                                <li><a href="#">One more separated link</a></li>
                            </ul>
                        </li>
                    </ul>
                    <form class="navbar-form navbar-left" role="search">
                        <div class="form-group">
                            <input type="text" class="form-control" placeholder="Search meeting">
                        </div>
                        <button type="submit" class="btn btn-default">Submit</button>
                    </form>
                    <ul class="nav navbar-nav navbar-right">
                        <li><p class="navbar-text">Logged in as: ${name} </p></li>
                        <li><form action="Logout">
                                <a href="#" onclick="$(this).closest('form').submit()">Logout</a>
                            </form></li>
                        
                    </ul>
                </div><!-- /.navbar-collapse -->
            </div><!-- /.container-fluid -->
        </nav>
                        
                        <br><br><br>
    
    <jsp:useBean id="showDirectionsMap" scope ="page"  class="sessionbeans.showDirectionsMap"/>
    <iframe
  width="600"
  height="450"
  frameborder="0" style="border:0"
  src= <% String query;
        query = showDirectionsMap.queryBuilder("Peschiera Borromeo", "Bussero");
        out.println(query);%> allowfullscreen>
</iframe>

<div> <% out.println(query); %> </div>
    </body>  
</html>
