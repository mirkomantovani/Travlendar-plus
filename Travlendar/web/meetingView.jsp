<%-- 
    Document   : meetingView
    Created on : 06-Dec-2017, 15:26:51
    Author     : matteo
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
         
         <link rel="stylesheet" href="css/meetingView.css" type="text/css">
         <link rel="stylesheet" href="css/navbar.css">
         <link href="./bootstrap/css/bootstrap.min.css" rel="stylesheet" media="screen">
         <link rel="stylesheet" href="css/simple.css">
         <link rel="stylesheet" href="css/button.css">
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
                        
        <div style="height: 50px"></div>
                        
        <div class="wrapper" action="MeetingVisualization">
  <header class="header">${m.name}</header>
  
  <aside class="sidebar">
      <button class="offset"> Update meeting </button>
      <button class="offset"> Delete meeting</button>  
  </aside>
  <article class="content">
    <p>
      Location:
    </p>
    <p>
      Date:
    </p>
    <p>
      Duration:
    </p>
  </article>
  <footer class="footer">
      <button class="offset"> View route </button>
  </footer>
</div>                
                        
    </body>
</html>
                        
                        
